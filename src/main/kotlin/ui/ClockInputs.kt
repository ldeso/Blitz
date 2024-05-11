// Copyright 2024 Léo de Souza
// SPDX-License-Identifier: Apache-2.0

package net.leodesouza.blitz.ui

import android.content.res.Configuration.ORIENTATION_LANDSCAPE
import android.content.res.Configuration.ORIENTATION_PORTRAIT
import android.os.Build
import androidx.activity.compose.PredictiveBackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.LayoutDirection
import kotlinx.coroutines.delay
import net.leodesouza.blitz.ui.components.LeaningSide
import net.leodesouza.blitz.ui.models.ClockState

/** What action is executed by a back gesture. */
enum class ClockBackAction { PAUSE, RESET }

/**
 * Effect making system back gestures pause or reset the clock.
 *
 * @param[clockStateProvider] Lambda for the current state of the clock.
 * @param[pause] Callback called to pause the clock.
 * @param[reset] Callback called to reset the clock.
 * @param[save] Callback called to save the time or configuration.
 * @param[restore] Callback called to restore the saved time or configuration.
 * @param[updateProgress] Callback called to update the progress of the back gesture.
 * @param[updateSwipeEdge] Callback called to update the swipe edge where the back gesture starts.
 */
@Composable
fun ClockBackHandler(
    clockStateProvider: () -> ClockState,
    pause: () -> Unit,
    reset: () -> Unit,
    save: () -> Unit,
    restore: () -> Unit,
    updateAction: (ClockBackAction) -> Unit,
    updateProgress: (Float) -> Unit,
    updateSwipeEdge: (Int) -> Unit,
) {
    val clockState = clockStateProvider()

    PredictiveBackHandler(enabled = clockState != ClockState.FULL_RESET) { backEvent ->
        // beginning of back gesture
        val action = if (clockState == ClockState.TICKING) {
            ClockBackAction.PAUSE
        } else {
            ClockBackAction.RESET
        }
        updateAction(action)
        if (action == ClockBackAction.PAUSE) save()

        try {
            var progress = 0F
            backEvent.collect {  // during progressive back gesture
                progress = it.progress
                updateProgress(progress)
                updateSwipeEdge(it.swipeEdge)
            }

            // completion of back gesture
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                while (progress < 1F) {
                    delay(1L)
                    progress += 0.01F
                    updateProgress(progress)
                }
                delay(100L)
            }
            when (action) {
                ClockBackAction.PAUSE -> pause()
                ClockBackAction.RESET -> reset()
            }
            if (action == ClockBackAction.PAUSE) restore()

        } finally {  // after back gesture
            updateProgress(0F)
        }
    }
}

/**
 * Modifier to control the chess clock through click events, dragging events and key presses.
 *
 * @param[dragSensitivity] How many minutes or seconds to add per dragged pixel.
 * @param[interactionSource] Mutable interaction source used to dispatch click events.
 * @param[clockStateProvider] Lambda for the current state of the clock.
 * @param[leaningSideProvider] Lambda for which side the device is currently leaning towards.
 * @param[displayOrientation] The [ORIENTATION_PORTRAIT] or [ORIENTATION_LANDSCAPE] of the display.
 * @param[layoutDirection] Whether the layout direction is left-to-right or right-to-left.
 * @param[start] Callback called to start the clock.
 * @param[play] Callback called to switch to the next player.
 * @param[save] Callback called to save the time or configuration.
 * @param[restore] Callback called to restore the saved time or configuration.
 */
fun Modifier.clockInput(
    dragSensitivity: Float,
    interactionSource: MutableInteractionSource,
    clockStateProvider: () -> ClockState,
    leaningSideProvider: () -> LeaningSide,
    displayOrientation: Int,  // ORIENTATION_PORTRAIT or ORIENTATION_LANDSCAPE
    layoutDirection: LayoutDirection,
    start: () -> Unit,
    play: () -> Unit,
    save: () -> Unit,
    restore: (addMinutes: Float, addSeconds: Float) -> Unit,
): Modifier = then(
    clickable(interactionSource = interactionSource, indication = null) {
        onClickEvent(clockState = clockStateProvider(), start = start, play = play)
    }
        .onKeyEvent {
            onKeyEvent(
                keyEvent = it,
                clockState = clockStateProvider(),
                layoutDirection = layoutDirection,
                save = save,
                restore = restore,
            )
        }
        .pointerInput(Unit) {
            detectHorizontalDragGestures(
                onDragStart = { onDragStart(clockState = clockStateProvider(), save = save) },
                onDragEnd = { onDragEnd(clockState = clockStateProvider(), play = play) },
                onHorizontalDrag = { _: PointerInputChange, dragAmount: Float ->
                    onHorizontalDrag(
                        addAmount = dragSensitivity * dragAmount,
                        clockState = clockStateProvider(),
                        leaningSide = leaningSideProvider(),
                        displayOrientation = displayOrientation,
                        layoutDirection = layoutDirection,
                        restore = restore,
                    )
                },
            )
        }
        .pointerInput(Unit) {
            detectVerticalDragGestures(
                onDragStart = { onDragStart(clockState = clockStateProvider(), save = save) },
                onDragEnd = { onDragEnd(clockState = clockStateProvider(), play = play) },
                onVerticalDrag = { _: PointerInputChange, dragAmount: Float ->
                    onVerticalDrag(
                        addAmount = dragSensitivity * dragAmount,
                        clockState = clockStateProvider(),
                        leaningSide = leaningSideProvider(),
                        displayOrientation = displayOrientation,
                        layoutDirection = layoutDirection,
                        restore = restore,
                    )
                },
            )
        },
)

/**
 * Start the clock or switch to the next player on click events.
 *
 * @param[clockState] Current state of the clock.
 * @param[start] Callback called to start the clock.
 * @param[play] Callback called to switch to the next player.
 */
private fun onClickEvent(clockState: ClockState, start: () -> Unit, play: () -> Unit) {
    when (clockState) {
        ClockState.PAUSED, ClockState.SOFT_RESET, ClockState.FULL_RESET -> start()
        ClockState.TICKING -> play()
        else -> Unit
    }
}

/**
 * Change the time or the configuration of the clock on key presses.
 *
 * @param[keyEvent] The key event to intercept.
 * @param[clockState] Current state of the clock.
 * @param[layoutDirection] Whether the layout direction is left-to-right or right-to-left.
 * @param[save] Callback called to save the time or configuration.
 * @param[restore] Callback called to restore the saved time or configuration.
 */
private fun onKeyEvent(
    keyEvent: KeyEvent,
    clockState: ClockState,
    layoutDirection: LayoutDirection,
    save: () -> Unit,
    restore: (addMinutes: Float, addSeconds: Float) -> Unit,
): Boolean {
    if (keyEvent.type == KeyEventType.KeyDown) {
        when (clockState) {
            ClockState.PAUSED, ClockState.SOFT_RESET, ClockState.FULL_RESET -> run {
                var addMinutes = 0F
                var addSeconds = 0F
                val isLtr = when (layoutDirection) {
                    LayoutDirection.Ltr -> true
                    LayoutDirection.Rtl -> false
                }

                when (keyEvent.key) {
                    Key.DirectionUp -> addSeconds = 1F
                    Key.DirectionDown -> addSeconds = -1F
                    Key.DirectionRight -> addMinutes = if (isLtr) 1F else -1F
                    Key.DirectionLeft -> addMinutes = if (isLtr) -1F else 1F
                    else -> return false
                }

                save()
                restore(addMinutes, addSeconds)

                return true
            }

            else -> return false
        }
    } else {
        return false
    }
}

/**
 * Save the current time/configuration if the clock is on pause at the beginning of a drag gesture.
 *
 * @param[clockState] Current state of the clock.
 * @param[save] Callback called to save the time or configuration.
 */
private fun onDragStart(clockState: ClockState, save: () -> Unit) {
    when (clockState) {
        ClockState.PAUSED, ClockState.SOFT_RESET, ClockState.FULL_RESET -> save()
        else -> Unit
    }
}

/**
 * Switch to the next player if the clock is ticking at the end of a drag gesture.
 *
 * @param[clockState] Current state of the clock.
 * @param[play] Callback called to switch to the next player.
 */
private fun onDragEnd(clockState: ClockState, play: () -> Unit) {
    if (clockState == ClockState.TICKING) {
        play()
    }
}

/**
 * Add seconds (in portrait orientation) or minutes (in landscape orientation) to the current time
 * or configuration of the clock during drag events.
 *
 * @param[addAmount] How many minutes or seconds to add.
 * @param[clockState] Current state of the clock.
 * @param[leaningSide] Which side the device is currently leaning towards.
 * @param[displayOrientation] The [ORIENTATION_PORTRAIT] or [ORIENTATION_LANDSCAPE] of the display.
 * @param[layoutDirection] Whether the layout direction is left-to-right or right-to-left.
 * @param[restore] Callback called to restore the saved time or configuration.
 */
private fun onHorizontalDrag(
    addAmount: Float,
    clockState: ClockState,
    leaningSide: LeaningSide,
    displayOrientation: Int,  // ORIENTATION_PORTRAIT or ORIENTATION_LANDSCAPE
    layoutDirection: LayoutDirection,
    restore: (addMinutes: Float, addSeconds: Float) -> Unit,
) {
    when (clockState) {
        ClockState.PAUSED, ClockState.SOFT_RESET, ClockState.FULL_RESET -> run {
            var addMinutes = 0F
            var addSeconds = 0F

            when (displayOrientation) {
                ORIENTATION_PORTRAIT -> addSeconds = when (leaningSide) {
                    LeaningSide.LEFT -> addAmount
                    LeaningSide.RIGHT -> -addAmount
                }

                ORIENTATION_LANDSCAPE -> addMinutes = when (layoutDirection) {
                    LayoutDirection.Ltr -> addAmount
                    LayoutDirection.Rtl -> -addAmount
                }
            }

            restore(addMinutes, addSeconds)
        }

        else -> Unit
    }
}

/**
 * Add minutes (in portrait orientation) or seconds (in landscape orientation) to the current time
 * or configuration of the clock during drag events.
 *
 * @param[addAmount] How many minutes or seconds to add.
 * @param[clockState] Current state of the clock.
 * @param[leaningSide] Which side the device is currently leaning towards.
 * @param[displayOrientation] The [ORIENTATION_PORTRAIT] or [ORIENTATION_LANDSCAPE] of the display.
 * @param[layoutDirection] Whether the layout direction is left-to-right or right-to-left.
 * @param[restore] Callback called to restore the saved time or configuration.
 */
private fun onVerticalDrag(
    addAmount: Float,
    clockState: ClockState,
    leaningSide: LeaningSide,
    displayOrientation: Int,  // ORIENTATION_PORTRAIT or ORIENTATION_LANDSCAPE
    layoutDirection: LayoutDirection,
    restore: (addMinutes: Float, addSeconds: Float) -> Unit,
) {
    when (clockState) {
        ClockState.PAUSED, ClockState.SOFT_RESET, ClockState.FULL_RESET -> run {
            var addMinutes = 0F
            var addSeconds = 0F
            val isLtr = when (layoutDirection) {
                LayoutDirection.Ltr -> true
                LayoutDirection.Rtl -> false
            }

            when (displayOrientation) {
                ORIENTATION_PORTRAIT -> addMinutes = when (leaningSide) {
                    LeaningSide.LEFT -> if (isLtr) addAmount else -addAmount
                    LeaningSide.RIGHT -> if (isLtr) -addAmount else addAmount
                }

                ORIENTATION_LANDSCAPE -> addSeconds = -addAmount
            }

            restore(addMinutes, addSeconds)
        }

        else -> Unit
    }
}

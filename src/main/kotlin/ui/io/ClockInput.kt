// Copyright 2024 Léo de Souza
// SPDX-License-Identifier: Apache-2.0

package net.leodesouza.blitz.ui.io

import android.content.res.Configuration.ORIENTATION_LANDSCAPE
import android.content.res.Configuration.ORIENTATION_PORTRAIT
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectVerticalDragGestures
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
import net.leodesouza.blitz.ui.components.LeaningSide
import net.leodesouza.blitz.ui.models.ClockState

/**
 * Modifier to control the chess clock through click events, dragging events and key presses.
 *
 * @param[dragSensitivity] How many minutes or seconds to add per dragged pixel.
 * @param[clockStateProvider] Lambda for the current state of the clock.
 * @param[leaningSideProvider] Lambda for which side the device is currently leaning towards.
 * @param[isBusyProvider] Lambda for whether the clock is currently busy.
 * @param[displayOrientation] The [ORIENTATION_PORTRAIT] or [ORIENTATION_LANDSCAPE] of the display.
 * @param[layoutDirection] Whether the layout direction is left-to-right or right-to-left.
 * @param[start] Callback called to start the clock.
 * @param[play] Callback called to switch to the next player.
 * @param[save] Callback called to save the time or configuration.
 * @param[restore] Callback called to restore the saved time or configuration.
 */
fun Modifier.clockInput(
    dragSensitivity: Float,
    clockStateProvider: () -> ClockState,
    leaningSideProvider: () -> LeaningSide,
    isBusyProvider: () -> Boolean,
    displayOrientation: Int,  // ORIENTATION_PORTRAIT or ORIENTATION_LANDSCAPE
    layoutDirection: LayoutDirection,
    start: () -> Unit,
    play: () -> Unit,
    save: () -> Unit,
    restore: (addMinutes: Float, addSeconds: Float) -> Unit,
): Modifier = clickable(interactionSource = null, indication = null) {
    onClickEvent(
        clockState = clockStateProvider(),
        isBusy = isBusyProvider(),
        start = start,
        play = play,
    )
}
    .onKeyEvent {
        onKeyEvent(
            keyEvent = it,
            clockState = clockStateProvider(),
            layoutDirection = layoutDirection,
            isBusy = isBusyProvider(),
            save = save,
            restore = restore,
        )
    }
    .pointerInput(Unit) {
        detectHorizontalDragGestures(
            onDragStart = {
                onDragStart(
                    clockState = clockStateProvider(), isBusy = isBusyProvider(), save = save,
                )
            },
            onDragEnd = {
                onDragEnd(
                    clockState = clockStateProvider(), isBusy = isBusyProvider(), play = play,
                )
            },
            onHorizontalDrag = { _: PointerInputChange, dragAmount: Float ->
                onHorizontalDrag(
                    addAmount = dragSensitivity * dragAmount,
                    clockState = clockStateProvider(),
                    leaningSide = leaningSideProvider(),
                    displayOrientation = displayOrientation,
                    layoutDirection = layoutDirection,
                    isBusy = isBusyProvider(),
                    restore = restore,
                )
            },
        )
    }
    .pointerInput(Unit) {
        detectVerticalDragGestures(
            onDragStart = {
                onDragStart(
                    clockState = clockStateProvider(), isBusy = isBusyProvider(), save = save,
                )
            },
            onDragEnd = {
                onDragEnd(
                    clockState = clockStateProvider(), isBusy = isBusyProvider(), play = play,
                )
            },
            onVerticalDrag = { _: PointerInputChange, dragAmount: Float ->
                onVerticalDrag(
                    addAmount = dragSensitivity * dragAmount,
                    clockState = clockStateProvider(),
                    leaningSide = leaningSideProvider(),
                    displayOrientation = displayOrientation,
                    layoutDirection = layoutDirection,
                    isBusy = isBusyProvider(),
                    restore = restore,
                )
            },
        )
    }

/**
 * Start the clock or switch to the next player on click events.
 *
 * @param[clockState] Current state of the clock.
 * @param[isBusy] Whether the clock is currently busy.
 * @param[start] Callback called to start the clock.
 * @param[play] Callback called to switch to the next player.
 */
private fun onClickEvent(
    clockState: ClockState, isBusy: Boolean, start: () -> Unit, play: () -> Unit,
) {
    if (!isBusy) {
        when (clockState) {
            ClockState.PAUSED, ClockState.SOFT_RESET, ClockState.FULL_RESET -> start()
            ClockState.TICKING -> play()
            ClockState.FINISHED -> Unit
        }
    }
}

/**
 * Change the time or the configuration of the clock on key presses.
 *
 * @param[keyEvent] The key event to intercept.
 * @param[clockState] Current state of the clock.
 * @param[layoutDirection] Whether the layout direction is left-to-right or right-to-left.
 * @param[isBusy] Whether the clock is currently busy.
 * @param[save] Callback called to save the time or configuration.
 * @param[restore] Callback called to restore the saved time or configuration.
 */
private fun onKeyEvent(
    keyEvent: KeyEvent,
    clockState: ClockState,
    layoutDirection: LayoutDirection,
    isBusy: Boolean,
    save: () -> Unit,
    restore: (addMinutes: Float, addSeconds: Float) -> Unit,
): Boolean {
    if (!isBusy && keyEvent.type == KeyEventType.KeyDown) {
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
 * @param[isBusy] Whether the clock is currently busy.
 * @param[save] Callback called to save the time or configuration.
 */
private fun onDragStart(clockState: ClockState, isBusy: Boolean, save: () -> Unit) {
    if (!isBusy) {
        when (clockState) {
            ClockState.PAUSED, ClockState.SOFT_RESET, ClockState.FULL_RESET -> save()
            else -> Unit
        }
    }
}

/**
 * Switch to the next player if the clock is ticking at the end of a drag gesture.
 *
 * @param[clockState] Current state of the clock.
 * @param[isBusy] Whether the clock is currently busy.
 * @param[play] Callback called to switch to the next player.
 */
private fun onDragEnd(clockState: ClockState, isBusy: Boolean, play: () -> Unit) {
    if (!isBusy && clockState == ClockState.TICKING) {
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
 * @param[isBusy] Whether the clock is currently busy.
 * @param[restore] Callback called to restore the saved time or configuration.
 */
private fun onHorizontalDrag(
    addAmount: Float,
    clockState: ClockState,
    leaningSide: LeaningSide,
    displayOrientation: Int,  // ORIENTATION_PORTRAIT or ORIENTATION_LANDSCAPE
    layoutDirection: LayoutDirection,
    isBusy: Boolean,
    restore: (addMinutes: Float, addSeconds: Float) -> Unit,
) {
    if (!isBusy) {
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
 * @param[isBusy] Whether the clock is currently busy.
 * @param[restore] Callback called to restore the saved time or configuration.
 */
private fun onVerticalDrag(
    addAmount: Float,
    clockState: ClockState,
    leaningSide: LeaningSide,
    displayOrientation: Int,  // ORIENTATION_PORTRAIT or ORIENTATION_LANDSCAPE
    layoutDirection: LayoutDirection,
    isBusy: Boolean,
    restore: (addMinutes: Float, addSeconds: Float) -> Unit,
) {
    if (!isBusy) {
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
}

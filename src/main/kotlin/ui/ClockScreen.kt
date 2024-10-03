// Copyright 2024 Léo de Souza
// SPDX-License-Identifier: Apache-2.0

package net.leodesouza.blitz.ui

import android.content.Context
import android.media.AudioManager
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import net.leodesouza.blitz.ui.components.AnimatedBackHandler
import net.leodesouza.blitz.ui.components.LeaningSide
import net.leodesouza.blitz.ui.components.LeaningSideHandler
import net.leodesouza.blitz.ui.components.OrientationHandler
import net.leodesouza.blitz.ui.components.ScopedBackHandler
import net.leodesouza.blitz.ui.components.ScopedEffectHandler
import net.leodesouza.blitz.ui.components.SwipeEdge
import net.leodesouza.blitz.ui.io.clockFeedback
import net.leodesouza.blitz.ui.io.clockInput
import net.leodesouza.blitz.ui.models.BackAction
import net.leodesouza.blitz.ui.models.ClockState

/**
 * Minimalist Fischer chess clock.
 *
 * Defaults to 5+3 Fischer timing: 5 minutes + 3 seconds per move. Touching the screen switches to
 * the next player. Horizontal and vertical dragging set the time and increment. Haptic feedback is
 * enabled by setting the ringtone to vibrate. Back gestures pause and reset the clock.
 *
 * @param[durationMinutes] Initial time for each player in minutes.
 * @param[incrementSeconds] Time increment in seconds.
 * @param[tickPeriodMillis] Period between ticks in milliseconds.
 * @param[dragSensitivity] How many minutes or seconds to add per dragged pixel.
 * @param[onClockStart] Callback called before the clock starts ticking.
 * @param[onClockStop] Callback called after the clock stops ticking.
 * @param[clockViewModel] ViewModel holding the state and logic for this screen.
 */
@Composable
fun ClockScreen(
    durationMinutes: Int = 5,
    incrementSeconds: Int = 3,
    tickPeriodMillis: Int = 100,
    dragSensitivity: Float = 0.01F,
    onClockStart: () -> Unit = {},
    onClockStop: () -> Unit = {},
    clockViewModel: ClockViewModel = viewModel {
        ClockViewModel(durationMinutes, incrementSeconds, tickPeriodMillis)
    },
) {
    val displayOrientation = LocalConfiguration.current.orientation
    val layoutDirection = LocalLayoutDirection.current
    val audioManager = LocalContext.current.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    val haptics = LocalHapticFeedback.current

    val whiteTime by clockViewModel.whiteTime.collectAsStateWithLifecycle()
    val blackTime by clockViewModel.blackTime.collectAsStateWithLifecycle()
    val clockState by clockViewModel.clockState.collectAsStateWithLifecycle()
    val playerState by clockViewModel.playerState.collectAsStateWithLifecycle()

    var orientation by remember { mutableIntStateOf(0) }
    var leaningSide by remember { mutableStateOf(LeaningSide.RIGHT) }
    var backEventSwipeEdge by remember {
        when (layoutDirection) {
            LayoutDirection.Ltr -> mutableStateOf(SwipeEdge.LEFT)
            LayoutDirection.Rtl -> mutableStateOf(SwipeEdge.RIGHT)
        }
    }
    var backEventProgress by remember { mutableFloatStateOf(0F) }
    var backEventAction by remember { mutableStateOf(BackAction.PAUSE) }
    val isBusy by remember { derivedStateOf { backEventProgress != 0F } }

    OrientationHandler { orientation = it }

    LeaningSideHandler(
        orientationProvider = { orientation }, leaningSideProvider = { leaningSide },
    ) {
        leaningSide = when (leaningSide) {
            LeaningSide.LEFT -> LeaningSide.RIGHT
            LeaningSide.RIGHT -> LeaningSide.LEFT
        }
    }

    AnimatedBackHandler(
        enabledProvider = { clockState != ClockState.FULL_RESET },
        onBackStart = {
            backEventAction = if (clockState == ClockState.TICKING) {
                clockViewModel.save()
                BackAction.PAUSE
            } else {
                BackAction.RESET
            }
        },
        onCompletion = {
            when (backEventAction) {
                BackAction.PAUSE -> run {
                    clockViewModel.pause()
                    clockViewModel.restore(isDecimalRestored = true)
                }

                BackAction.RESET -> clockViewModel.reset()
            }
        },
        updateSwipeEdge = { backEventSwipeEdge = it },
        updateProgress = { backEventProgress = it },
    )

    ScopedBackHandler(enabledProvider = { isBusy }) {}

    ScopedEffectHandler(
        enabledProvider = { clockState == ClockState.TICKING }, effect = onClockStart,
    )

    ScopedEffectHandler(
        enabledProvider = { clockState == ClockState.PAUSED || clockState == ClockState.FINISHED },
        effect = onClockStop,
    )

    Box(
        modifier = Modifier.clockInput(
            dragSensitivity = dragSensitivity,
            clockStateProvider = { clockState },
            leaningSideProvider = { leaningSide },
            isBusyProvider = { isBusy },
            displayOrientation = displayOrientation,
            layoutDirection = layoutDirection,
            start = {
                onClockStart()
                clockViewModel.start()
                clockFeedback(audioManager, haptics)
            },
            play = {
                clockViewModel.play()
                clockFeedback(audioManager, haptics)
            },
            save = clockViewModel::save,
            restore = clockViewModel::restore,
        ),
    ) {
        ClockContent(
            whiteTimeProvider = { whiteTime },
            blackTimeProvider = { blackTime },
            clockStateProvider = { clockState },
            playerStateProvider = { playerState },
            leaningSideProvider = { leaningSide },
            backEventActionProvider = { backEventAction },
            backEventProgressProvider = { backEventProgress },
            backEventSwipeEdgeProvider = { backEventSwipeEdge },
        )
    }
}

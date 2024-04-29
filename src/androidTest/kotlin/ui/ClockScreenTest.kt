// Copyright 2024 Léo de Souza
// SPDX-License-Identifier: Apache-2.0

package net.leodesouza.blitz.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import org.junit.Rule
import org.junit.Test

class ClockScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun isDisplayed() {
        composeTestRule.setContent {
            ClockScreen()
        }

        composeTestRule.onRoot().assertIsDisplayed()
    }
}

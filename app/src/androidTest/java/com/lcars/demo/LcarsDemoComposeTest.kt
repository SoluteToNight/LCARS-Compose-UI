package com.lcars.demo

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicText
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.unit.dp
import com.lcars.ui.LcarsResponsiveScaffold
import org.junit.Rule
import org.junit.Test

class LcarsDemoComposeTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun advisoryButton_togglesWeatherAlert() {
        composeRule.setContent {
            DemoLcarsTheme {
                LcarsDemoScreen(modifier = Modifier.fillMaxSize())
            }
        }

        composeRule.onNodeWithText("STORM ADVISORY").performClick()
        composeRule.onNodeWithText("STORM ADVISORY ACTIVE / PRESSURE DROP DETECTED").assertIsDisplayed()
        composeRule.onNodeWithText("CLEAR ADVISORY").assertIsDisplayed()
    }

    @Test
    fun weatherDemo_showsCurrentConditionsAndForecast() {
        composeRule.setContent {
            DemoLcarsTheme {
                LcarsDemoScreen(modifier = Modifier.fillMaxSize())
            }
        }

        composeRule.onNodeWithText("CURRENT CONDITIONS").assertIsDisplayed()
        composeRule.onNodeWithText("FORECAST MATRIX").assertIsDisplayed()
        composeRule.onNodeWithText("28 DEGREES / CLOUDY").assertIsDisplayed()
    }

    @Test
    fun weatherDemo_usesLowerDecksPaletteTitle() {
        composeRule.setContent {
            DemoLcarsTheme {
                LcarsDemoScreen(modifier = Modifier.fillMaxSize())
            }
        }

        composeRule.onNodeWithText("ATMOSPHERIC CONDITIONS").assertIsDisplayed()
        composeRule.onNodeWithText("CURRENT CONDITIONS").assertIsDisplayed()
    }

    @Test
    fun paddVariantNavigation_showsStandardPaddDemoAndStyleSwitcher() {
        composeRule.setContent {
            PaddVariantDemoScreen(onBack = {}, modifier = Modifier.fillMaxSize())
        }

        composeRule.onNodeWithText("USS RAVEN - DATABASE 83-S28").assertIsDisplayed()
        composeRule.onNodeWithText("HANSEN FAMILY").assertIsDisplayed()
        composeRule.onNodeWithText("STD").assertIsDisplayed()
        composeRule.onNodeWithText("CLASSIC").assertIsDisplayed()
    }

    @Test
    fun responsiveScaffold_selectsExpectedSlotForSize() {
        composeRule.setContent {
            Box(modifier = Modifier.size(width = 320.dp, height = 240.dp)) {
                LcarsResponsiveScaffold(
                    compactWidth = 200.dp,
                    compactLandscapeHeight = 100.dp,
                    portrait = { BasicText("PORTRAIT SLOT") },
                    compactLandscape = { BasicText("COMPACT SLOT") },
                    wideLandscape = { BasicText("WIDE SLOT") },
                )
            }
        }

        composeRule.onNodeWithText("WIDE SLOT").assertIsDisplayed()
    }
}

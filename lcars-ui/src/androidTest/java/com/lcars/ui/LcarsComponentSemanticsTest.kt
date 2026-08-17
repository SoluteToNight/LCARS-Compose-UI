package com.lcars.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.semantics.SemanticsActions
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.assertIsToggleable
import androidx.compose.ui.test.assertIsNotSelected
import androidx.compose.ui.test.hasProgressBarRangeInfo
import androidx.compose.ui.test.hasStateDescription
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performSemanticsAction
import com.lcars.ui.controls.LcarsButton
import com.lcars.ui.controls.LcarsSegmentedControl
import com.lcars.ui.controls.LcarsToggle
import com.lcars.ui.display.LcarsAlertBanner
import com.lcars.ui.display.LcarsProgressBar
import com.lcars.ui.display.LcarsSegmentedSlider
import com.lcars.ui.theme.LcarsTheme
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class LcarsComponentSemanticsTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun disabledButton_remainsVisibleAndDisabled() {
        composeRule.setContent {
            LcarsTheme {
                LcarsButton(text = "disabled", onClick = {}, enabled = false)
            }
        }

        composeRule.onNodeWithText("DISABLED")
            .assertIsDisplayed()
            .assertIsNotEnabled()
    }

    @Test
    fun toggle_exposesCheckedDescriptionAndDisabledState() {
        composeRule.setContent {
            LcarsTheme {
                LcarsToggle(
                    checked = true,
                    onCheckedChange = {},
                    checkedLabel = "armed",
                    uncheckedLabel = "standby",
                    enabled = false,
                )
            }
        }

        composeRule.onNode(hasStateDescription("armed"))
            .assertIsToggleable()
            .assertIsNotEnabled()
    }

    @Test
    fun segmentedControl_exposesSelectedTabs() {
        composeRule.setContent {
            LcarsTheme {
                LcarsSegmentedControl(
                    options = listOf("nav", "sensor"),
                    selectedOption = "sensor",
                    onOptionSelected = {},
                )
            }
        }

        composeRule.onNodeWithText("NAV").assertIsNotSelected().assertIsEnabled()
        composeRule.onNodeWithText("SENSOR").assertIsSelected().assertIsEnabled()
    }

    @Test
    fun progressAndSlider_exposeRangesAndSliderAction() {
        var sliderValue by mutableIntStateOf(3)
        composeRule.setContent {
            LcarsTheme {
                LcarsProgressBar(progress = 0.64f)
                LcarsSegmentedSlider(
                    value = sliderValue,
                    onValueChange = { sliderValue = it },
                    totalSegments = 10,
                )
            }
        }

        composeRule.onNode(
            hasProgressBarRangeInfo(ProgressBarRangeInfo(0.64f, 0f..1f)),
        ).assertIsDisplayed()
        composeRule.onNode(
            hasProgressBarRangeInfo(ProgressBarRangeInfo(3f, 0f..10f, 9)),
        ).performSemanticsAction(SemanticsActions.SetProgress) { setProgress ->
            setProgress(7f)
        }

        composeRule.runOnIdle { assertEquals(7, sliderValue) }
    }

    @Test
    fun activeAlert_usesAssertiveLiveRegion() {
        composeRule.setContent {
            LcarsTheme {
                LcarsAlertBanner(message = "red alert", active = true)
            }
        }

        composeRule.onNodeWithText("RED ALERT").assertIsDisplayed()
        composeRule.onNode(
            SemanticsMatcher.expectValue(
                SemanticsProperties.LiveRegion,
                LiveRegionMode.Assertive,
            ),
        ).assertIsDisplayed()
    }
}

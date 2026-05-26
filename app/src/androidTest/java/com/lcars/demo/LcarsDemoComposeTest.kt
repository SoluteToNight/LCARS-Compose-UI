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
    fun alertButton_togglesStatusText() {
        composeRule.setContent {
            DemoLcarsTheme {
                LcarsDemoScreen(modifier = Modifier.fillMaxSize())
            }
        }

        composeRule.onNodeWithText("TRIGGER SYSTEM FLASH").performClick()
        composeRule.onNodeWithText("DANGER: ALERT ACTIVE").assertIsDisplayed()
    }

    @Test
    fun catalogNavigation_showsComponentCatalog() {
        composeRule.setContent {
            DemoLcarsTheme {
                LcarsDemoScreen(modifier = Modifier.fillMaxSize())
            }
        }

        composeRule.onNodeWithText("CATALOG").performClick()
        composeRule.onNodeWithText("COMPONENT CATALOG").assertIsDisplayed()
        composeRule.onNodeWithText("DYNAMIC STATES").assertIsDisplayed()
    }

    @Test
    fun segmentedControl_changesCatalogMode() {
        composeRule.setContent {
            DemoLcarsTheme {
                LcarsDemoScreen(modifier = Modifier.fillMaxSize())
            }
        }

        composeRule.onNodeWithText("CATALOG").performClick()
        composeRule.onNodeWithText("COMM").performClick()
        composeRule.onNodeWithText("MODE COMM // SECURITY STANDBY // DIALOG IDLE").assertIsDisplayed()
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
    fun toggle_changesCatalogState() {
        composeRule.setContent {
            DemoLcarsTheme {
                LcarsDemoScreen(modifier = Modifier.fillMaxSize())
            }
        }

        composeRule.onNodeWithText("CATALOG").performClick()
        composeRule.onNodeWithText("STANDBY").performClick()
        composeRule.onNodeWithText("MODE NAV // SECURITY ARMED // DIALOG IDLE").assertIsDisplayed()
    }

    @Test
    fun dialog_confirmUpdatesCatalogState() {
        composeRule.setContent {
            DemoLcarsTheme {
                LcarsDemoScreen(modifier = Modifier.fillMaxSize())
            }
        }

        composeRule.onNodeWithText("CATALOG").performClick()
        composeRule.onNodeWithText("OPEN DIALOG").performClick()
        composeRule.onNodeWithText("AUTHORIZE").performClick()
        composeRule.onNodeWithText("MODE NAV // SECURITY STANDBY // DIALOG CONFIRMED").assertIsDisplayed()
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

package com.lcars.demo

import android.graphics.Bitmap
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.test.captureToImage
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.test.platform.app.InstrumentationRegistry
import java.io.File
import java.io.FileOutputStream
import org.junit.Rule
import org.junit.Test

class ScreenshotCaptureTest {
    @get:Rule
    val composeRule = createComposeRule()

    private fun saveScreenshot(name: String) {
        composeRule.waitForIdle()
        val imageBitmap = composeRule.onRoot().captureToImage()
        val bitmap = imageBitmap.asAndroidBitmap()
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val dir = context.getExternalFilesDir(null) ?: File("/sdcard/Download")
        if (!dir.exists()) {
            dir.mkdirs()
        }
        val file = File(dir, "$name.png")
        FileOutputStream(file).use { out ->
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
        }
    }

    @Test
    fun captureAllScreenshots() {
        // 1. Hub Screen
        composeRule.setContent {
            Box(modifier = Modifier.fillMaxSize()) {
                DemoHubScreen(
                    onOpenCatalog = {},
                    onOpenWeather = {},
                    onOpenPadd = {},
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }
        saveScreenshot("demo_hub_screen")

        // 2. Weather Screen (Normal)
        composeRule.setContent {
            Box(modifier = Modifier.fillMaxSize()) {
                DemoLcarsTheme {
                    WeatherSystemDemoScreen(modifier = Modifier.fillMaxSize())
                }
            }
        }
        saveScreenshot("weather_screen_normal")

        // 3. Weather Screen (Storm Alert)
        composeRule.onNodeWithText("STORM ADVISORY").performClick()
        saveScreenshot("weather_screen_alert")

        // 4. PADD Screen (Standard)
        composeRule.setContent {
            Box(modifier = Modifier.fillMaxSize()) {
                PaddVariantDemoScreen(onBack = {}, modifier = Modifier.fillMaxSize())
            }
        }
        saveScreenshot("padd_screen_standard")

        // 5. PADD Screen (Classic)
        composeRule.onNodeWithText("CLASSIC").performClick()
        saveScreenshot("padd_screen_classic")

        // 6. Catalog Screen - Tab 1 (Controls)
        composeRule.setContent {
            Box(modifier = Modifier.fillMaxSize()) {
                ComponentShowcaseDemoScreen(modifier = Modifier.fillMaxSize())
            }
        }
        saveScreenshot("catalog_tab1_controls")

        // 7. Catalog Screen - Tab 2 (Data)
        composeRule.onNodeWithText("DATA").performClick()
        saveScreenshot("catalog_tab2_data")

        // 8. Catalog Screen - Tab 3 (Motion)
        composeRule.onNodeWithText("MOTION").performClick()
        saveScreenshot("catalog_tab3_motion")

        // 9. Catalog Screen - Tab 4 (Sensors)
        composeRule.onNodeWithText("SENSORS").performClick()
        saveScreenshot("catalog_tab4_sensors")

        // 10. Catalog Screen - Tab 5 (Patterns)
        composeRule.onNodeWithText("PATTERNS").performClick()
        saveScreenshot("catalog_tab5_patterns")
    }
}

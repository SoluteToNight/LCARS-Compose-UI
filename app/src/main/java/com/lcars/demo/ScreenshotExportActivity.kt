package com.lcars.demo

import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import java.io.File
import java.io.FileOutputStream
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ScreenshotExportActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        window.addFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true)
            setTurnScreenOn(true)
        }

        setContent {
            ScreenshotRunner()
        }
    }

    @Composable
    private fun ScreenshotRunner() {
        var currentStep by remember { mutableIntStateOf(0) }

        Box(modifier = Modifier.fillMaxSize()) {
            when (currentStep) {
                0 -> DemoHubScreen(
                    onOpenCatalog = {},
                    onOpenWeather = {},
                    onOpenPadd = {},
                    modifier = Modifier.fillMaxSize(),
                )
                1 -> DemoLcarsTheme {
                    WeatherSystemDemoScreen(modifier = Modifier.fillMaxSize())
                }
                2 -> PaddVariantDemoScreen(
                    onBack = {},
                    modifier = Modifier.fillMaxSize(),
                )
                3 -> ComponentShowcaseDemoScreen(
                    initialTab = 0,
                    onBack = {},
                    modifier = Modifier.fillMaxSize(),
                )
                4 -> ComponentShowcaseDemoScreen(
                    initialTab = 1,
                    onBack = {},
                    modifier = Modifier.fillMaxSize(),
                )
                5 -> ComponentShowcaseDemoScreen(
                    initialTab = 2,
                    onBack = {},
                    modifier = Modifier.fillMaxSize(),
                )
                6 -> ComponentShowcaseDemoScreen(
                    initialTab = 3,
                    onBack = {},
                    modifier = Modifier.fillMaxSize(),
                )
                7 -> ComponentShowcaseDemoScreen(
                    initialTab = 4,
                    onBack = {},
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }

        remember {
            lifecycleScope.launch {
                val stepNames = listOf(
                    "demo_hub_screen",
                    "weather_screen_normal",
                    "padd_screen_standard",
                    "catalog_tab1_controls",
                    "catalog_tab2_data",
                    "catalog_tab3_motion",
                    "catalog_tab4_sensors",
                    "catalog_tab5_patterns",
                )

                delay(1000) // Initial wait for layout
                for (i in stepNames.indices) {
                    currentStep = i
                    delay(800) // Wait for render and recomposition
                    captureAndSave(stepNames[i])
                }
                val marker = File(getExternalFilesDir(null), "DONE.txt")
                marker.writeText("EXPORT COMPLETE")
            }
            true
        }
    }

    private fun captureAndSave(name: String) {
        val decorView = window.decorView
        val width = if (decorView.width > 0) decorView.width else 3200
        val height = if (decorView.height > 0) decorView.height else 2136
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        decorView.draw(canvas)
        val dir = getExternalFilesDir(null) ?: filesDir
        val file = File(dir, "$name.png")
        FileOutputStream(file).use { out ->
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
        }
    }
}

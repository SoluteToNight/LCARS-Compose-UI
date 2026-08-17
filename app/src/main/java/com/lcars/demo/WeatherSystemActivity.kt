package com.lcars.demo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.lcars.ui.theme.LcarsPreset

class WeatherSystemActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        window.addFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true)
            setTurnScreenOn(true)
        }
        setContent {
            var preset by rememberSaveable { mutableStateOf(LcarsPreset.NemesisBlueUltra) }
            DemoLcarsTheme(preset = preset) {
                WeatherSystemDemoScreen(
                    modifier = Modifier.fillMaxSize(),
                    preset = preset,
                    onTogglePreset = {
                        preset = if (preset == LcarsPreset.NemesisBlueUltra) {
                            LcarsPreset.ClassicUltra
                        } else {
                            LcarsPreset.NemesisBlueUltra
                        }
                    },
                )
            }
        }
    }
}

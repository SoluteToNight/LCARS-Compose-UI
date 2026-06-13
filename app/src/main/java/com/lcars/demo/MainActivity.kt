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
import com.lcars.ui.LcarsStyle

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var currentStyle by rememberSaveable { mutableStateOf(LcarsStyle.LowerDecksPadd) }
            DemoLcarsTheme(style = currentStyle) {
                LcarsDemoScreen(
                    modifier = Modifier.fillMaxSize(),
                    style = currentStyle,
                    onToggleStyle = {
                        currentStyle = if (currentStyle == LcarsStyle.LowerDecksPadd) {
                            LcarsStyle.ClassicUltra
                        } else {
                            LcarsStyle.LowerDecksPadd
                        }
                    }
                )
            }
        }
    }
}

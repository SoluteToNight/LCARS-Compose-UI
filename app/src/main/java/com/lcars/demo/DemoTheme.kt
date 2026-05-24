package com.lcars.demo

import androidx.compose.runtime.Composable
import com.lcars.ui.LcarsAdaptiveTheme

@Composable
fun DemoLcarsTheme(content: @Composable () -> Unit) {
    LcarsAdaptiveTheme(content = content)
}

package com.lcars.demo

import androidx.compose.runtime.Composable
import com.lcars.ui.LcarsAdaptiveTheme
import com.lcars.ui.LcarsStyle

@Composable
fun DemoLcarsTheme(content: @Composable () -> Unit) {
    LcarsAdaptiveTheme(style = LcarsStyle.LowerDecksPadd, content = content)
}

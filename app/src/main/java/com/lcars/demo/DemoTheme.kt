package com.lcars.demo

import androidx.compose.runtime.Composable
import com.lcars.ui.LcarsAdaptiveTheme
import com.lcars.ui.LcarsStyle

@Composable
fun DemoLcarsTheme(style: LcarsStyle = LcarsStyle.LowerDecksPadd, content: @Composable () -> Unit) {
    LcarsAdaptiveTheme(style = style, content = content)
}

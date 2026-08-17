package com.lcars.demo

import androidx.compose.runtime.Composable
import com.lcars.ui.theme.LcarsPreset
import com.lcars.ui.theme.LcarsTheme
import com.lcars.ui.theme.spec

@Composable
fun DemoLcarsTheme(preset: LcarsPreset = LcarsPreset.LowerDecksPadd, content: @Composable () -> Unit) {
    LcarsTheme(spec = preset.spec, content = content)
}

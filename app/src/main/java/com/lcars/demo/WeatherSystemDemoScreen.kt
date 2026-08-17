package com.lcars.demo

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.lcars.ui.theme.LcarsPreset

@Composable
fun WeatherSystemDemoScreen(
    modifier: Modifier = Modifier,
    preset: LcarsPreset = LcarsPreset.LowerDecksPadd,
    onTogglePreset: (() -> Unit)? = null,
) {
    AtmosphericConditionsDemoScreen(
        modifier = modifier,
        style = preset,
        onToggleStyle = onTogglePreset,
    )
}

@Preview(widthDp = 1280, heightDp = 720, showBackground = true)
@Composable
private fun WeatherSystemWidePreview() {
    DemoLcarsTheme {
        WeatherSystemDemoScreen(modifier = Modifier.fillMaxSize())
    }
}

@Preview(widthDp = 844, heightDp = 390, showBackground = true)
@Composable
private fun WeatherSystemCompactPreview() {
    DemoLcarsTheme {
        WeatherSystemDemoScreen(modifier = Modifier.fillMaxSize())
    }
}

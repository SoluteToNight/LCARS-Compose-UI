package com.lcars.demo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

import com.lcars.ui.LcarsSegmentedControl
import com.lcars.ui.LcarsStyle
import com.lcars.ui.LocalLcarsColors
import com.lcars.ui.LocalLcarsSpacing

private enum class DemoMode(val label: String) {
    Weather("weather panel"),
    Components("components"),
}

@Composable
fun LcarsDemoScreen(
    modifier: Modifier = Modifier,
    style: LcarsStyle = LcarsStyle.LowerDecksPadd,
    onToggleStyle: (() -> Unit)? = null,
) {
    val colors = LocalLcarsColors.current
    val spacing = LocalLcarsSpacing.current
    var mode by rememberSaveable { mutableStateOf(DemoMode.Weather) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(colors.background)
            .padding(spacing.gapStandard),
        verticalArrangement = Arrangement.spacedBy(spacing.gapStandard),
    ) {
        LcarsSegmentedControl(
            options = DemoMode.entries.map { it.label },
            selectedOption = mode.label,
            onOptionSelected = { selected ->
                mode = DemoMode.entries.first { it.label == selected }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(44.dp),
        )
        when (mode) {
            DemoMode.Weather -> AtmosphericConditionsDemoScreen(
                modifier = Modifier.weight(1f),
                style = style,
                onToggleStyle = onToggleStyle,
            )
            DemoMode.Components -> ComponentShowcaseDemoScreen(
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@Preview(widthDp = 1280, heightDp = 720, showBackground = true)
@Composable
private fun LcarsDemoWideLandscapePreview() {
    DemoLcarsTheme {
        LcarsDemoScreen(modifier = Modifier.fillMaxSize())
    }
}

@Preview(widthDp = 844, heightDp = 390, showBackground = true)
@Composable
private fun LcarsDemoCompactLandscapePreview() {
    DemoLcarsTheme {
        LcarsDemoScreen(modifier = Modifier.fillMaxSize())
    }
}

@Preview(widthDp = 390, heightDp = 820, showBackground = true)
@Composable
private fun LcarsDemoPortraitPreview() {
    DemoLcarsTheme {
        LcarsDemoScreen(modifier = Modifier.fillMaxSize())
    }
}

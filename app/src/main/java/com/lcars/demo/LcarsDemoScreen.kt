package com.lcars.demo

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

import com.lcars.ui.LcarsStyle

@Composable
fun LcarsDemoScreen(
    modifier: Modifier = Modifier,
    style: LcarsStyle = LcarsStyle.LowerDecksPadd,
    onToggleStyle: (() -> Unit)? = null,
) {
    AtmosphericConditionsDemoScreen(
        modifier = modifier,
        style = style,
        onToggleStyle = onToggleStyle,
    )
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

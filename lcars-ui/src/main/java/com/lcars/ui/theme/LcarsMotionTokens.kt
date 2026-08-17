package com.lcars.ui.theme

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.delay
import kotlin.random.Random

/**
 * LCARS Smooth Motion Tokens & Helpers.
 * Designed for 60fps/120fps fluid transitions matching Stage 9 aesthetic.
 */
object LcarsMotionTokens {
    /** Continuous linear flow spec for EPS power pipes and conduits */
    val FlowLinearSpec: AnimationSpec<Float> = infiniteRepeatable(
        animation = tween(durationMillis = 2000, easing = LinearEasing),
        repeatMode = RepeatMode.Restart,
    )

    /** Smooth rhythmic pulse spec for diagnostics and highlighted warning zones */
    val PulseSmoothSpec: AnimationSpec<Float> = infiniteRepeatable(
        animation = tween(durationMillis = 1000, easing = FastOutSlowInEasing),
        repeatMode = RepeatMode.Reverse,
    )

    /** Smooth targeting and tracking spec for crosshairs and brackets */
    val TargetingSpec: AnimationSpec<Float> = spring(
        dampingRatio = 0.85f,
        stiffness = 250f,
    )

    /** State transition spec for button color adjustments */
    val ColorTransitionSpec: AnimationSpec<Color> = tween(durationMillis = 200, easing = FastOutSlowInEasing)

    /** State transition spec for float/size adjustments */
    val FloatTransitionSpec: AnimationSpec<Float> = tween(durationMillis = 200, easing = FastOutSlowInEasing)
}

/**
 * Returns a continuously looping 0f..1f phase offset for smooth flowing energy conduits.
 */
@Composable
fun rememberLcarsSmoothFlow(
    durationMillis: Int = 2000,
    label: String = "LcarsSmoothFlow",
): State<Float> {
    val transition = rememberInfiniteTransition(label = label)
    return transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = durationMillis, easing = LinearEasing),
            repeatMode = RepeatMode.Restart,
        ),
        label = "${label}_Phase",
    )
}

/**
 * Returns a smooth breathing alpha value between minAlpha and maxAlpha.
 */
@Composable
fun rememberLcarsSmoothPulse(
    durationMillis: Int = 1000,
    minAlpha: Float = 0.25f,
    maxAlpha: Float = 1.0f,
    label: String = "LcarsSmoothPulse",
): State<Float> {
    val transition = rememberInfiniteTransition(label = label)
    return transition.animateFloat(
        initialValue = minAlpha,
        targetValue = maxAlpha,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = durationMillis, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "${label}_Alpha",
    )
}

/**
 * Generates an active, smoothly updating telemetry/diagnostic matrix.
 */
@Composable
fun rememberLcarsDataStream(
    rows: Int = 6,
    columns: Int = 4,
    refreshIntervalMillis: Long = 300L,
): State<List<List<String>>> {
    val state = remember {
        mutableStateOf(generateRandomMatrix(rows, columns))
    }

    LaunchedEffect(rows, columns, refreshIntervalMillis) {
        while (true) {
            delay(refreshIntervalMillis)
            state.value = generateRandomMatrix(rows, columns)
        }
    }
    return state
}

private fun generateRandomMatrix(rows: Int, columns: Int): List<List<String>> {
    return List(rows) {
        List(columns) {
            when (Random.nextInt(4)) {
                0 -> "%04d".format(Random.nextInt(10000))
                1 -> "%02d %03d".format(Random.nextInt(100), Random.nextInt(1000))
                2 -> "%05d".format(Random.nextInt(100000))
                else -> "%02d".format(Random.nextInt(100))
            }
        }
    }
}

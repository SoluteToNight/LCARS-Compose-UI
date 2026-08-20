package com.lcars.ui.layout

import com.lcars.ui.theme.*
import com.lcars.ui.foundation.*
import com.lcars.ui.controls.*
import com.lcars.ui.display.*
import com.lcars.ui.scene.*
import com.lcars.ui.padd.*

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.graphicsLayer
import kotlinx.coroutines.delay

/**
 * State controller for the Star Trek LCARS Console Bootup / Power-On Cascade sequence.
 */
@Stable
class LcarsCascadeState(
    initialActiveStep: Int = 0,
) {
    var activeStep by mutableIntStateOf(initialActiveStep)
        internal set

    var isComplete by mutableStateOf(false)
        internal set

    var triggerKey by mutableIntStateOf(0)
        private set

    /**
     * Re-triggers the power-on cascade sequence from the beginning.
     */
    fun trigger() {
        activeStep = 0
        isComplete = false
        triggerKey++
    }
}

@Composable
fun rememberLcarsCascadeState(): LcarsCascadeState = remember { LcarsCascadeState() }

val LocalLcarsCascadeState = staticCompositionLocalOf<LcarsCascadeState?> { null }

/**
 * Wraps a screen or layout container to provide a synchronized LCARS power-on cascade reveal.
 *
 * Components decorated with [Modifier.lcarsCascade] will light up sequentially from index 0 to [totalSteps].
 */
@Composable
fun LcarsCascadeContainer(
    totalSteps: Int,
    modifier: Modifier = Modifier,
    state: LcarsCascadeState = rememberLcarsCascadeState(),
    stepDurationMillis: Int = 45,
    enabled: Boolean = true,
    content: @Composable () -> Unit,
) {
    val motionMode = LcarsTheme.motionMode

    LaunchedEffect(state.triggerKey, enabled, motionMode) {
        if (!enabled || motionMode != LcarsMotionMode.System) {
            state.activeStep = totalSteps
            state.isComplete = true
            return@LaunchedEffect
        }

        state.activeStep = 0
        state.isComplete = false

        for (step in 1..totalSteps) {
            delay(stepDurationMillis.toLong())
            state.activeStep = step
        }
        state.isComplete = true
    }

    CompositionLocalProvider(LocalLcarsCascadeState provides state) {
        Box(modifier = modifier) {
            content()
        }
    }
}

/**
 * Modifier to participate in the LCARS Power-On Cascade reveal sequence.
 *
 * @param index The step index at which this element illuminates (0-indexed).
 */
fun Modifier.lcarsCascade(
    index: Int,
): Modifier = this

@Composable
fun Modifier.lcarsCascadeItem(
    index: Int,
): Modifier {
    val cascadeState = LocalLcarsCascadeState.current ?: return this
    val motionMode = LcarsTheme.motionMode

    if (motionMode != LcarsMotionMode.System) {
        return this
    }

    return this.graphicsLayer {
        val visible = cascadeState.activeStep >= index
        alpha = if (visible) 1f else 0f
    }
}

package com.lcars.ui

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.lcars.ui.controls.LcarsSegmentState
import com.lcars.ui.controls.resolveLcarsSegmentState
import com.lcars.ui.controls.resolveLcarsToggleLabel
import com.lcars.ui.display.LcarsLogSeverity
import com.lcars.ui.display.generateLcarsNumberMatrix
import com.lcars.ui.display.generateLcarsStarCoords
import com.lcars.ui.display.logSeverityColor
import com.lcars.ui.layout.LcarsResponsiveMode
import com.lcars.ui.layout.resolveLcarsResponsiveMode
import com.lcars.ui.padd.resolveLcarsPhonePaddMetrics
import com.lcars.ui.scene.generateLcarsStars
import com.lcars.ui.theme.LcarsPreset
import com.lcars.ui.theme.LcarsPresets
import com.lcars.ui.theme.LcarsSizeClass
import com.lcars.ui.theme.resolveLcarsSizeClass
import com.lcars.ui.theme.spec
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test

class LcarsComponentLogicTest {
    @Test
    fun generatedDisplays_useStableSeeds() {
        assertEquals(
            generateLcarsNumberMatrix(3, 5, 407),
            generateLcarsNumberMatrix(3, 5, 407),
        )
        assertNotEquals(
            generateLcarsStarCoords(2, 6, 1701),
            generateLcarsStarCoords(2, 6, 1702),
        )
        assertEquals(generateLcarsStars(118), generateLcarsStars(118))
    }

    @Test
    fun sizeClasses_followComposeBreakpoints() {
        assertEquals(LcarsSizeClass.Compact, resolveLcarsSizeClass(390.dp))
        assertEquals(LcarsSizeClass.Medium, resolveLcarsSizeClass(700.dp))
        assertEquals(LcarsSizeClass.Expanded, resolveLcarsSizeClass(840.dp))
        assertEquals(
            LcarsResponsiveMode.CompactLandscape,
            resolveLcarsResponsiveMode(844.dp, 390.dp),
        )
    }

    @Test
    fun presets_matchLcars242ReferenceTokens() {
        assertEquals(Color(0xFFFF9900), LcarsPresets.ClassicUltra.colorScheme.commandPrimary)
        assertEquals(Color(0xFF2266FF), LcarsPresets.NemesisBlueUltra.colorScheme.framePrimary)
        assertEquals(Color(0xFF5588EE), LcarsPresets.LowerDecksPadd.colorScheme.framePrimary)
        assertEquals(0.40f, LcarsPresets.NemesisBlueUltra.dimensions.barPrimaryFraction)
        assertEquals(0.10f, LcarsPresets.LowerDecksPadd.dimensions.barPrimaryFraction)
        assertEquals(LcarsPresets.NemesisBlueUltra, LcarsPreset.NemesisBlueUltra.spec)
    }

    @Test
    fun paddMetrics_resolveForPortraitWidths() {
        assertEquals(32.dp, resolveLcarsPhonePaddMetrics(320.dp, 680.dp).railWidth)
        assertEquals(38.dp, resolveLcarsPhonePaddMetrics(390.dp, 820.dp).railWidth)
        assertEquals(42.dp, resolveLcarsPhonePaddMetrics(440.dp, 900.dp).railWidth)
    }

    @Test
    fun controls_resolveInteractionState() {
        assertEquals(
            LcarsSegmentState.Selected,
            resolveLcarsSegmentState("NAV", "NAV", setOf("NAV", "COMM")),
        )
        assertEquals(
            LcarsSegmentState.Disabled,
            resolveLcarsSegmentState("SENSOR", "NAV", setOf("NAV", "COMM")),
        )
        assertEquals("armed", resolveLcarsToggleLabel(true, "armed", "standby"))
        assertEquals("standby", resolveLcarsToggleLabel(false, "armed", "standby"))
    }

    @Test
    fun severity_usesSemanticThemeColors() {
        val colors = LcarsPresets.ClassicUltra.colorScheme
        assertEquals(colors.lightBlue, logSeverityColor(LcarsLogSeverity.Info, colors))
        assertEquals(colors.tacticalGreen, logSeverityColor(LcarsLogSeverity.Success, colors))
        assertEquals(colors.monoAmber, logSeverityColor(LcarsLogSeverity.Warning, colors))
        assertEquals(colors.alertRed, logSeverityColor(LcarsLogSeverity.Alert, colors))
    }

    @Test
    fun motionScheme_matchesStarTrekAnimationTimings() {
        val motion = LcarsPresets.ClassicUltra.motionScheme
        assertEquals(300, motion.alertCriticalStepMillis)
        assertEquals(400, motion.alertWarningStepMillis)
        assertEquals(600, motion.alertAdvisoryStepMillis)
        assertEquals(120, motion.tactileFlashMillis)
        assertEquals(45, motion.cascadeStepMillis)
        assertEquals(100, motion.telemetryTickMillis)
        assertEquals(1000, motion.reactorPulseMillis)
    }

    @Test
    fun cascadeState_triggersCorrectly() {
        val cascade = com.lcars.ui.layout.LcarsCascadeState(initialActiveStep = 0)
        assertEquals(0, cascade.activeStep)
        assertEquals(false, cascade.isComplete)
        cascade.trigger()
        assertEquals(1, cascade.triggerKey)
    }
}

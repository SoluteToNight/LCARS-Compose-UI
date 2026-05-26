package com.lcars.ui

import androidx.compose.ui.unit.dp
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test

class LcarsComponentLogicTest {
    @Test
    fun numberMatrix_usesStableSeed() {
        val first = generateLcarsNumberMatrix(rows = 3, columns = 5, seed = 407)
        val second = generateLcarsNumberMatrix(rows = 3, columns = 5, seed = 407)
        val changed = generateLcarsNumberMatrix(rows = 3, columns = 5, seed = 408)

        assertEquals(first, second)
        assertNotEquals(first, changed)
    }

    @Test
    fun starCoords_usesStableSeed() {
        assertEquals(
            generateLcarsStarCoords(count = 2, digits = 6, seed = 1701),
            generateLcarsStarCoords(count = 2, digits = 6, seed = 1701),
        )
        assertNotEquals(
            generateLcarsStarCoords(count = 2, digits = 6, seed = 1701),
            generateLcarsStarCoords(count = 2, digits = 6, seed = 1702),
        )
    }

    @Test
    fun starChart_usesStableSeed() {
        assertEquals(generateLcarsStars(seed = 118), generateLcarsStars(seed = 118))
        assertNotEquals(generateLcarsStars(seed = 118), generateLcarsStars(seed = 119))
    }

    @Test
    fun responsiveMode_matchesDemoBreakpoints() {
        assertEquals(
            LcarsResponsiveMode.Portrait,
            resolveLcarsResponsiveMode(width = 390.dp, height = 820.dp),
        )
        assertEquals(
            LcarsResponsiveMode.CompactLandscape,
            resolveLcarsResponsiveMode(width = 844.dp, height = 390.dp),
        )
        assertEquals(
            LcarsResponsiveMode.WideLandscape,
            resolveLcarsResponsiveMode(width = 1280.dp, height = 720.dp),
        )
    }

    @Test
    fun adaptiveProfile_marksNonWideLayoutsCompact() {
        assertEquals(
            LcarsAdaptiveProfile(LcarsResponsiveMode.Portrait, compact = true),
            resolveLcarsAdaptiveProfile(width = 390.dp, height = 820.dp),
        )
        assertEquals(
            LcarsAdaptiveProfile(LcarsResponsiveMode.CompactLandscape, compact = true),
            resolveLcarsAdaptiveProfile(width = 844.dp, height = 390.dp),
        )
        assertEquals(
            LcarsAdaptiveProfile(LcarsResponsiveMode.WideLandscape, compact = false),
            resolveLcarsAdaptiveProfile(width = 1280.dp, height = 720.dp),
        )
    }

    @Test
    fun adaptiveSpacing_keepsWideDefaultsAndCompactsGeometry() {
        val base = LcarsSpacing()

        assertEquals(base, resolveLcarsAdaptiveSpacing(base, LcarsAdaptiveProfile()))
        assertEquals(
            52.dp,
            resolveLcarsAdaptiveSpacing(
                base,
                LcarsAdaptiveProfile(LcarsResponsiveMode.Portrait, compact = true),
            ).buttonMinHeight,
        )
        assertEquals(
            44.dp,
            resolveLcarsAdaptiveSpacing(
                base,
                LcarsAdaptiveProfile(LcarsResponsiveMode.CompactLandscape, compact = true),
            ).buttonMinHeight,
        )
    }

    @Test
    fun adaptiveThemePolicy_keepsTypographyAtDefaultLogicalResolution() {
        val base = LcarsTypography()

        assertEquals(34f, base.header.fontSize.value)
        assertEquals(22f, base.button.fontSize.value)
        assertEquals(24f, base.telemetry.fontSize.value)
        assertEquals(16f, base.labelSmall.fontSize.value)
    }

    @Test
    fun standardPaddStyle_mapsToPhonePaddPaletteAndCompactSpacing() {
        val colors = LcarsStyle.StandardPadd.colors()
        val spacing = LcarsStyle.StandardPadd.spacing()

        assertEquals(lcarsPhonePaddColors(), colors)
        assertEquals(42.dp, spacing.buttonMinHeight)
        assertEquals(20.dp, spacing.barHeight)
        assertEquals(8.dp, spacing.panelPadding)
    }

    @Test
    fun phonePaddMetrics_resolveForPortraitPhoneWidths() {
        assertEquals(32.dp, resolveLcarsPhonePaddMetrics(width = 320.dp, height = 680.dp).railWidth)
        assertEquals(38.dp, resolveLcarsPhonePaddMetrics(width = 390.dp, height = 820.dp).railWidth)
        assertEquals(42.dp, resolveLcarsPhonePaddMetrics(width = 440.dp, height = 900.dp).railWidth)
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
    fun logSeverity_mapsToThemeColors() {
        val colors = LcarsColors()

        assertEquals(colors.lightBlue, logSeverityColor(LcarsLogSeverity.Info, colors))
        assertEquals(colors.tacticalGreen, logSeverityColor(LcarsLogSeverity.Success, colors))
        assertEquals(colors.monoAmber, logSeverityColor(LcarsLogSeverity.Warning, colors))
        assertEquals(colors.alertRed, logSeverityColor(LcarsLogSeverity.Alert, colors))
    }
}

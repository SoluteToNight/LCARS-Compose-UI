package com.lcars.ui

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

enum class LcarsStyle {
    ClassicUltra,
    LowerDecks,
    LowerDecksPadd,
    NemesisBlueUltra,
}

fun LcarsStyle.colors(): LcarsColors = when (this) {
    LcarsStyle.ClassicUltra -> LcarsColors()
    LcarsStyle.LowerDecks -> LcarsColors(
        monoAmber = Color(0xFFFF9911),
        auxiliaryTan = Color(0xFFFFEECC),
        lightBlue = Color(0xFFFFCC99),
        violet = Color(0xFFFFAA44),
        tacticalGreen = Color(0xFFCC5500),
        alertRed = Color(0xFFFF4400),
        panel = Color(0xFF050505),
        text = Color(0xFFFFEECC),
        a1 = Color(0xFFFF7700),
        a2 = Color(0xFFFFAA44),
        a3 = Color(0xFFFFCC99),
        a4 = Color(0xFFFFEECC),
        a5 = Color(0xFFFF9911),
        a6 = Color(0xFFCC5500),
        a7 = Color(0xFFFF4400),
        a8 = Color(0xFFFFCC99),
        a9 = Color(0xFFCC5500),
        blue = Color(0xFFFF7700),
        butterscotch = Color(0xFFFFAA44),
        almondCreme = Color(0xFFFFEECC),
        classicRed = Color(0xFFFF4400),
        tomato = Color(0xFFFF4400),
        gray = Color(0xFFCC5500),
        spaceWhite = Color(0xFFFFEECC),
    )
    LcarsStyle.LowerDecksPadd -> LcarsColors(
        monoAmber = Color(0xFF66CCFF),
        auxiliaryTan = Color(0xFF99CCFF),
        lightBlue = Color(0xFF5588EE),
        violet = Color(0xFF7799DD),
        tacticalGreen = Color(0xFF88FFFF),
        alertRed = Color(0xFFFF3300),
        panel = Color(0xFF05070C),
        text = Color(0xFFBBDDFF),
        a1 = Color(0xFF88FFFF),
        a2 = Color(0xFF66CCFF),
        a3 = Color(0xFF99CCFF),
        a4 = Color(0xFFBBDDFF),
        a5 = Color(0xFF7799DD),
        a6 = Color(0xFF455580),
        a7 = Color(0xFF344470),
        a8 = Color(0xFF5588EE),
        a9 = Color(0xFF455580),
        blue = Color(0xFF5588EE),
        butterscotch = Color(0xFF66CCFF),
        almondCreme = Color(0xFFBBDDFF),
        classicRed = Color(0xFF455580),
        tomato = Color(0xFFFF3300),
        gray = Color(0xFF344470),
        spaceWhite = Color(0xFFBBDDFF),
    )
    LcarsStyle.NemesisBlueUltra -> LcarsColors(
        monoAmber = Color(0xFFFFCC99),
        auxiliaryTan = Color(0xFFCCAA88),
        lightBlue = Color(0xFF6699FF),
        violet = Color(0xFF9966CC),
        tacticalGreen = Color(0xFF99CC33),
        alertRed = Color(0xFFCC2233),
        panel = Color(0xFF040408),
        text = Color(0xFFEBF0FF),
        a1 = Color(0xFF2233FF),
        a2 = Color(0xFF6699FF),
        a3 = Color(0xFF88BBFF),
        a4 = Color(0xFFEBF0FF),
        a5 = Color(0xFFFF8833),
        a6 = Color(0xFF9966CC),
        a7 = Color(0xFF2323FF),
        a8 = Color(0xFF52526A),
        a9 = Color(0xFFCC6666),
        blue = Color(0xFF2233FF),
        butterscotch = Color(0xFFFFCC99),
        almondCreme = Color(0xFFEBF0FF),
        classicRed = Color(0xFF6699FF),
        tomato = Color(0xFFFF7744),
        gray = Color(0xFF52526A),
        spaceWhite = Color(0xFFEBF0FF),
    )
}

fun LcarsStyle.spacing(): LcarsSpacing = when (this) {
    LcarsStyle.ClassicUltra,
    LcarsStyle.NemesisBlueUltra -> LcarsSpacing()
    LcarsStyle.LowerDecks,
    LcarsStyle.LowerDecksPadd -> LcarsSpacing(
        gapStandard = 4.dp,
        gapLarge = 8.dp,
        buttonMinHeight = 54.dp,
        barHeight = 28.dp,
        elbowThickness = 34.dp,
        panelPadding = 10.dp,
        scaffoldControlWidth = 150.dp,
        commandRailWidth = 150.dp,
        commandRailCompactWidth = 112.dp,
    )
}

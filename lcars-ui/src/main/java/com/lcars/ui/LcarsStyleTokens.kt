package com.lcars.ui

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

enum class LcarsStyle {
    StandardPadd,
    ClassicUltra,
    LowerDecks,
    LowerDecksPadd,
    NemesisBlueUltra,
}

object LcarsClassicColors {
    val africanViolet = Color(0xFFCC99FF)
    val almond = Color(0xFFFFAA90)
    val almondCreme = Color(0xFFFFBBAA)
    val blue = Color(0xFF5566FF)
    val bluey = Color(0xFF8899FF)
    val butterscotch = Color(0xFFFF9966)
    val gold = Color(0xFFFFAA00)
    val goldenOrange = Color(0xFFFF9900)
    val gray = Color(0xFF666688)
    val green = Color(0xFF999933)
    val ice = Color(0xFF99CCFF)
    val lilac = Color(0xFFCC55FF)
    val limaBean = Color(0xFFCCCC66)
    val magenta = Color(0xFFCC5599)
    val mars = Color(0xFFFF2200)
    val moonlitViolet = Color(0xFF9966FF)
    val orange = Color(0xFFFF8800)
    val peach = Color(0xFFFF8866)
    val red = Color(0xFFCC4444)
    val sky = Color(0xFFAAAAFF)
    val spaceWhite = Color(0xFFF5F6FA)
    val sunflower = Color(0xFFFFCC99)
    val tomato = Color(0xFFFF5555)
    val violetCreme = Color(0xFFDDBBFF)
}

fun LcarsStyle.colors(): LcarsColors = when (this) {
    LcarsStyle.StandardPadd -> lcarsPhonePaddColors()
    LcarsStyle.ClassicUltra -> LcarsColors(
        monoAmber = LcarsClassicColors.goldenOrange,
        auxiliaryTan = LcarsClassicColors.almondCreme,
        lightBlue = LcarsClassicColors.bluey,
        violet = LcarsClassicColors.africanViolet,
        tacticalGreen = LcarsClassicColors.gold,
        alertRed = LcarsClassicColors.mars,
        panel = Color(0xFF050505),
        text = LcarsClassicColors.spaceWhite,
        a1 = LcarsClassicColors.orange,
        a2 = LcarsClassicColors.butterscotch,
        a3 = LcarsClassicColors.almond,
        a4 = LcarsClassicColors.sunflower,
        a5 = LcarsClassicColors.gold,
        a6 = LcarsClassicColors.lilac,
        a7 = LcarsClassicColors.moonlitViolet,
        a8 = LcarsClassicColors.violetCreme,
        a9 = LcarsClassicColors.magenta,
        blue = LcarsClassicColors.blue,
        butterscotch = LcarsClassicColors.butterscotch,
        almondCreme = LcarsClassicColors.almondCreme,
        classicRed = LcarsClassicColors.red,
        tomato = LcarsClassicColors.tomato,
        gray = LcarsClassicColors.gray,
        spaceWhite = LcarsClassicColors.spaceWhite,

        // Semantic mapping for Weather Screen (TNG Command/Operations)
        weatherFrame = LcarsClassicColors.red,
        weatherBtnStyle = LcarsClassicColors.gold,
        weatherBtnSecondary = LcarsClassicColors.butterscotch,
        weatherBtnInactive = LcarsClassicColors.orange,
        weatherActiveAccent = LcarsClassicColors.gold,
        weatherInactiveAccent = LcarsClassicColors.butterscotch,
        weatherSubHighlight = LcarsClassicColors.almondCreme,
        weatherMeterInactive = LcarsClassicColors.gray,
        weatherSensorBar = LcarsClassicColors.red,
        weatherSensorLabel = LcarsClassicColors.gold,
    )
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
    LcarsStyle.StandardPadd -> LcarsSpacing(
        gapStandard = 4.dp,
        gapLarge = 8.dp,
        buttonMinWidth = 76.dp,
        buttonMinHeight = 42.dp,
        barHeight = 20.dp,
        elbowThickness = 24.dp,
        panelPadding = 8.dp,
        scaffoldControlWidth = 96.dp,
        commandRailWidth = 104.dp,
        commandRailCompactWidth = 82.dp,
    )
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

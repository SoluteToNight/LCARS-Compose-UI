package com.lcars.ui.layout

import com.lcars.ui.theme.*
import com.lcars.ui.foundation.*
import com.lcars.ui.controls.*
import com.lcars.ui.display.*
import com.lcars.ui.scene.*
import com.lcars.ui.padd.*

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun LcarsMainConsole(
    leftWingContent: @Composable ColumnScope.() -> Unit,
    mainDeckContent: @Composable ColumnScope.() -> Unit,
    modifier: Modifier = Modifier,
    leftWingWeight: Float = 0.28f,
) {
    val colors = LocalLcarsColors.current
    val gap = LocalLcarsSpacing.current.gapStandard

    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .background(colors.background)
            .padding(gap),
    ) {
        val resolvedLeftWingWeight = if (maxWidth < 620.dp) 0.34f else leftWingWeight
        Row(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .weight(resolvedLeftWingWeight)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.spacedBy(gap),
                content = leftWingContent,
            )
            Spacer(modifier = Modifier.width(gap))
            Column(
                modifier = Modifier
                    .weight(1f - resolvedLeftWingWeight)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.spacedBy(gap),
                content = mainDeckContent,
            )
        }
    }
}

package com.jayasuryat.uigame.composable.topbar.started

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jayasuryat.uigame.logic.GameProgress
import com.jayasuryat.util.LogCompositions

@Composable
internal fun GameStartedTopBar(
    modifier: Modifier = Modifier,
    gameProgress: State<GameProgress>,
    startTime: Long,
) {

    LogCompositions(name = "GameStartedTopBar")

    Row(
        modifier = modifier,
    ) {

        LogCompositions(name = "GameStartedTopBar\$Row")

        TickerChip(startTime = startTime)

        Spacer(modifier = Modifier.width(16.dp))

        GameProgressChip(gameProgress = gameProgress)
    }
}

@Preview
@Composable
private fun Preview() {

    val progress = remember {
        mutableStateOf(
            GameProgress(
                totalMinesCount = 10,
                flaggedMinesCount = 7,
            )
        )
    }

    GameStartedTopBar(
        modifier = Modifier.wrapContentSize(),
        startTime = System.currentTimeMillis(),
        gameProgress = progress,
    )
}
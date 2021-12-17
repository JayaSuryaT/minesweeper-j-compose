package com.jayasuryat.difficultyselection

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.rememberPagerState
import com.jayasuryat.difficultyselection.logic.GameDifficulty

@OptIn(ExperimentalPagerApi::class)
@Composable
fun DifficultySelectionScreen(
    difficulties: List<GameDifficulty> = listOf(
        GameDifficulty.Easy,
        GameDifficulty.Medium,
        GameDifficulty.Hard,
        GameDifficulty.Extreme,
    ),
    onDifficultySelected: (difficulty: GameDifficulty) -> Unit,
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colors.background),
    ) {

        val pagerState = rememberPagerState()

        Box(
            modifier = Modifier
                .align(alignment = Alignment.CenterHorizontally)
                .fillMaxHeight(fraction = 0.33f),
            contentAlignment = Alignment.Center,
        ) {

            Text(
                text = "Minesweeper",
                fontSize = 40.sp,
                color = MaterialTheme.colors.secondary,
                textAlign = TextAlign.Center,
            )
        }

        LevelPager(
            pagerState = pagerState,
            difficultyLevels = difficulties,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(fraction = 0.33f),
        )

        Spacer(
            modifier = Modifier.height(64.dp),
        )

        StartButton(
            modifier = Modifier.align(alignment = Alignment.CenterHorizontally),
            onStartClicked = {
                val difficulty = difficulties[pagerState.currentPage]
                onDifficultySelected(difficulty)
            },
        )
    }
}

@Composable
@Preview
private fun Preview() {

    DifficultySelectionScreen(
        onDifficultySelected = {},
    )
}
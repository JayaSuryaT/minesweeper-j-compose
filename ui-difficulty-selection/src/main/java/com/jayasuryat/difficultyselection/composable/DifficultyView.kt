/*
 * Copyright 2022 Jaya Surya Thotapalli
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jayasuryat.difficultyselection.composable

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.LocalOverScrollConfiguration
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import com.jayasuryat.difficultyselection.logic.DifficultyItem

@OptIn(ExperimentalPagerApi::class, ExperimentalFoundationApi::class)
@Composable
internal fun DifficultyView(
    modifier: Modifier = Modifier,
    pagerState: PagerState,
    difficultyItems: List<DifficultyItem>,
) {

    CompositionLocalProvider(
        LocalOverScrollConfiguration provides null
    ) {

        HorizontalPager(
            modifier = modifier,
            count = difficultyItems.size,
            state = pagerState,
        ) { page ->

            DifficultyItem(
                modifier = Modifier.fillMaxSize(),
                difficulty = difficultyItems[page],
            )
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Preview
@Composable
private fun Preview(
    @PreviewParameter(GameDifficultyListParamProvider::class) difficultyItems: List<DifficultyItem>,
) {

    DifficultyView(
        modifier = Modifier.fillMaxSize(),
        pagerState = rememberPagerState(),
        difficultyItems = difficultyItems
    )
}

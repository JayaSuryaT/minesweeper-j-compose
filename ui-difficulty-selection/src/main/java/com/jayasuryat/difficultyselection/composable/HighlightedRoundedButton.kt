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

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
internal fun HighlightedRoundedButton(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit,
) {

    Text(
        modifier = modifier
            .clip(RoundedCornerShape(100f))
            .border(
                width = 4.dp,
                color = MaterialTheme.colors.primary,
                shape = RoundedCornerShape(100f),
            )
            .clickable { onClick() }
            .padding(
                vertical = 12.dp,
                horizontal = 40.dp,
            ),
        text = text,
        style = MaterialTheme.typography.body1.copy(
            fontSize = 18.sp,
            fontWeight = FontWeight.Normal,
            color = MaterialTheme.colors.onBackground,
        )
    )
}

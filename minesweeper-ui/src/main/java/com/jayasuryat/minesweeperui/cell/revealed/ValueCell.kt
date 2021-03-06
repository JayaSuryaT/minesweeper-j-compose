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
package com.jayasuryat.minesweeperui.cell.revealed

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.min
import com.jayasuryat.minesweeperui.cell.VALUE_CELL_TEXT_COVER_PERCENT
import com.jayasuryat.minesweeperui.model.DisplayCell
import com.jayasuryat.minesweeperui.theme.msColors
import com.jayasuryat.util.LogCompositions
import com.jayasuryat.util.sp

@Composable
internal fun ValueCell(
    modifier: Modifier = Modifier,
    displayCell: DisplayCell.Cell.ValueCell,
    onClick: () -> Unit,
) {

    LogCompositions(name = "ValueCell")

    BoxWithConstraints(
        modifier = modifier
            .aspectRatio(1f)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
                onClick = onClick
            ),
        contentAlignment = Alignment.Center,
    ) {

        val minSize = min(minWidth, minHeight)
        val maxSize = min(maxWidth, maxHeight)
        val size = min(minSize, maxSize)

        val fontSize = getFontSize(width = maxWidth, height = maxHeight)
        // val paddingBottom = maxHeight / 20

        Spacer(
            modifier = Modifier
                .size(size)
                .clip(CircleShape)
                .border(
                    width = size * 0.02f * displayCell.value,
                    color = MaterialTheme.colors.onBackground
                        .copy(alpha = 1f - getAlphaForValue(displayCell.value)),
                    shape = CircleShape
                ),
        )

        Text(
            modifier = Modifier
                .wrapContentSize()
                .fillMaxWidth(),
            text = displayCell.value.toString(),
            fontSize = fontSize,
            fontWeight = FontWeight.Black,
            textAlign = TextAlign.Center,
            color = MaterialTheme.msColors.text,
        )
    }
}

@Stable
private fun getAlphaForValue(value: Int): Float {

    return when (value) {
        1 -> 0.85f
        2 -> 0.65f
        else -> 0.4f
    } / 2
}

@Composable
@Stable
@ReadOnlyComposable
private fun getFontSize(
    width: Dp,
    height: Dp,
): TextUnit {

    val minSize = minOf(width, height)
    val availableSize = minSize * VALUE_CELL_TEXT_COVER_PERCENT
    return availableSize.sp()
}

@Preview(heightDp = 600, widthDp = 600)
@Preview(heightDp = 60, widthDp = 60)
@Composable
private fun Preview() {

    val cell = DisplayCell.Cell.ValueCell(
        value = 1,
    )

    ValueCell(
        displayCell = cell,
        modifier = Modifier.fillMaxSize(),
        onClick = {},
    )
}

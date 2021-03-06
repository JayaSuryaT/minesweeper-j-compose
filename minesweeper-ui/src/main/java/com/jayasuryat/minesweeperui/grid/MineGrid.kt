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
package com.jayasuryat.minesweeperui.grid

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.jayasuryat.minesweeperengine.model.block.GridSize
import com.jayasuryat.minesweeperengine.model.block.Position
import com.jayasuryat.minesweeperui.action.CellInteractionListener
import com.jayasuryat.minesweeperui.cell.AnimatedRawCell
import com.jayasuryat.minesweeperui.cell.RawCell
import com.jayasuryat.minesweeperui.config.LocalGridAnimationConfig
import com.jayasuryat.minesweeperui.model.GridLayoutInformation
import com.jayasuryat.util.LogCompositions
import com.jayasuryat.util.dp
import com.jayasuryat.util.floatValue

@Composable
internal fun MineGrid(
    modifier: Modifier,
    horizontalPadding: Dp = 16.dp,
    gridInfo: GridLayoutInformation,
    actionListener: CellInteractionListener,
) {

    LogCompositions(name = "MineGrid")

    val enableCellAnimation = LocalGridAnimationConfig.current.enableCellAnimations

    BoxWithConstraints(
        modifier = modifier,
    ) {

        LogCompositions(name = "MineGrid/BoxWithConstraints")

        val width = maxWidth
        val gridSize = gridInfo.gridSize

        val cellSize = getCellSize(
            gridSize = gridSize,
            width = width - (horizontalPadding * 2),
        )

        Grid(
            parentHeight = maxHeight,
            horizontalPadding = horizontalPadding,
            gridInfo = gridInfo,
            actionListener = actionListener,
            cellSize = cellSize,
            enableCellAnimation = enableCellAnimation,
        )
    }
}

@Composable
private fun Grid(
    parentHeight: Dp,
    horizontalPadding: Dp,
    gridInfo: GridLayoutInformation,
    actionListener: CellInteractionListener,
    cellSize: Float,
    enableCellAnimation: Boolean,
) {

    LogCompositions(name = "Grid")

    val centerOffset = (parentHeight / 2).floatValue() - (cellSize * gridInfo.gridSize.rows / 2)
    val paddingOffset = horizontalPadding.floatValue()

    gridInfo.displayCells.forEach { cellData ->

        val offset = cellData.position.asOffset(cellSize = cellSize)

        if (enableCellAnimation) {

            AnimatedRawCell(
                modifier = Modifier
                    .size(cellSize.dp())
                    .graphicsLayer {
                        translationX = offset.x + paddingOffset
                        translationY = offset.y + centerOffset
                    },
                displayCell = cellData,
                interactionListener = actionListener,
            )
        } else {

            RawCell(
                modifier = Modifier
                    .size(cellSize.dp())
                    .graphicsLayer {
                        translationX = offset.x + paddingOffset
                        translationY = offset.y + centerOffset
                    },
                displayCell = cellData,
                interactionListener = actionListener,
            )
        }
    }
}

@Composable
@Stable
@ReadOnlyComposable
private fun getCellSize(
    gridSize: GridSize,
    width: Dp,
): Float {
    val defCellWidth = width / gridSize.columns
    return defCellWidth.floatValue()
}

@Stable
private fun Position.asOffset(
    cellSize: Float,
): Offset {
    val xOff = cellSize * this.column
    val yOff = cellSize * this.row
    return Offset(x = xOff, y = yOff)
}

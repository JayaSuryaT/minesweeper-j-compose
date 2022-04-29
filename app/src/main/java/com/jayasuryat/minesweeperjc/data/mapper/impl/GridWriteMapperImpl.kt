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
package com.jayasuryat.minesweeperjc.data.mapper.impl

import com.jayasuryat.data.model.Cell
import com.jayasuryat.minesweeperengine.model.block.Position
import com.jayasuryat.minesweeperengine.model.cell.MineCell
import com.jayasuryat.minesweeperengine.model.cell.RawCell
import com.jayasuryat.minesweeperjc.data.mapper.definition.GridWriteMapper
import com.jayasuryat.data.model.Grid as DGrid
import com.jayasuryat.minesweeperengine.model.grid.Grid as GGrid

class GridWriteMapperImpl : GridWriteMapper {

    override fun map(
        startTime: Long,
        endTime: Long?,
        input: GGrid,
    ): DGrid {

        val cells = input.cells.map { row ->

            row.map { rawCell ->

                val mineCell = when (rawCell) {
                    is RawCell.RevealedCell -> rawCell
                    is RawCell.UnrevealedCell -> rawCell.asRevealed()
                }.cell

                val value = when (mineCell) {
                    is MineCell.Mine -> MappingConstants.CELL_MINE_VALUE
                    is MineCell.ValuedCell -> mineCell.value
                }

                val isRevealed = rawCell is RawCell.RevealedCell
                val isFlagged = !isRevealed && rawCell is RawCell.UnrevealedCell.FlaggedCell

                Cell(
                    value = value,
                    position = rawCell.position.toPosition(),
                    revealed = isRevealed,
                    flagged = isFlagged,
                )
            }
        }

        return DGrid(
            rows = input.gridSize.rows,
            columns = input.gridSize.columns,
            totalMines = input.totalMines,
            startTime = startTime,
            endTime = endTime,
            grid = cells,
        )
    }

    private fun Position.toPosition(): Cell.Companion.Position = Cell.Companion.Position(
        row = row,
        column = column,
    )
}
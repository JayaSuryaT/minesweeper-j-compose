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
package com.jayasuryat.minesweeperengine.controller.impl

import com.jayasuryat.minesweeperengine.controller.ActionHandler
import com.jayasuryat.minesweeperengine.controller.MinefieldController
import com.jayasuryat.minesweeperengine.controller.impl.handler.CellFlagger
import com.jayasuryat.minesweeperengine.controller.impl.handler.CellRevealer
import com.jayasuryat.minesweeperengine.controller.impl.handler.ValueCellRevealer
import com.jayasuryat.minesweeperengine.controller.impl.handler.helper.GameEndRevealer
import com.jayasuryat.minesweeperengine.controller.impl.handler.helper.GameSuccessEvaluator
import com.jayasuryat.minesweeperengine.controller.impl.handler.helper.RadiallySorter
import com.jayasuryat.minesweeperengine.controller.impl.handler.helper.ValueNeighbourCalculator
import com.jayasuryat.minesweeperengine.controller.model.MinefieldAction
import com.jayasuryat.minesweeperengine.controller.model.MinefieldEvent
import com.jayasuryat.minesweeperengine.model.grid.Grid
import com.jayasuryat.util.exhaustive
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

public class GameController(
    private val cellReveler: ActionHandler<MinefieldAction.OnCellRevealed>,
    private val cellFlagger: ActionHandler<MinefieldAction.OnFlagToggled>,
    private val valueCellReveler: ActionHandler<MinefieldAction.OnValueCellClicked>,
) : MinefieldController {

    override suspend fun onAction(
        action: MinefieldAction,
        mineGrid: Grid,
    ): MinefieldEvent = withContext(Dispatchers.Default) {
        reduceActionToEvent(
            action = action,
            mineGrid = mineGrid
        )
    }

    private suspend fun reduceActionToEvent(
        action: MinefieldAction,
        mineGrid: Grid,
    ): MinefieldEvent {

        return when (action) {

            is MinefieldAction.OnCellRevealed -> cellReveler.onAction(
                action = action,
                grid = mineGrid,
            )

            is MinefieldAction.OnValueCellClicked -> valueCellReveler.onAction(
                action = action,
                grid = mineGrid,
            )

            is MinefieldAction.OnFlagToggled -> cellFlagger.onAction(
                action = action,
                grid = mineGrid,
            )
        }.exhaustive
    }

    public companion object {

        public fun getDefault(): GameController {
            val radiallySorter = RadiallySorter()
            val gameEndRevealer = GameEndRevealer(radiallySorter = radiallySorter)
            val successEvaluator = GameSuccessEvaluator()
            val neighbourCalculator = ValueNeighbourCalculator()
            return GameController(
                cellReveler = CellRevealer(
                    gameEndRevealer = gameEndRevealer,
                    radiallySorter = radiallySorter,
                    successEvaluator = successEvaluator,
                    neighbourCalculator = neighbourCalculator,
                ),
                cellFlagger = CellFlagger(),
                valueCellReveler = ValueCellRevealer(
                    gameEndRevealer = gameEndRevealer,
                    radiallySorter = radiallySorter,
                    successEvaluator = successEvaluator,
                    neighbourCalculator = neighbourCalculator,
                ),
            )
        }
    }
}

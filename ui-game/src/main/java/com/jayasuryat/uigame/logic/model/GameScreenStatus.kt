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
package com.jayasuryat.uigame.logic.model

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import com.jayasuryat.minesweeperui.action.CellInteractionListener
import com.jayasuryat.minesweeperui.config.GridAnimationConfig
import com.jayasuryat.minesweeperui.model.GridLayoutInformation

@Stable
internal sealed interface GameScreenStatus {

    @Immutable
    object Loading : GameScreenStatus

    @Stable
    data class Loaded(
        val layoutInformation: GridLayoutInformation,
        val interactionListener: CellInteractionListener,
        val gameState: State<GameState>,
        val gameProgress: State<GameProgress>,
        val animationConfig: GridAnimationConfig,
    ) : GameScreenStatus
}

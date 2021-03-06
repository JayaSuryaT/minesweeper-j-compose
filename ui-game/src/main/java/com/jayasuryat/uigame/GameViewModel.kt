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
package com.jayasuryat.uigame

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.jayasuryat.minesweeperengine.controller.MinefieldController
import com.jayasuryat.minesweeperui.config.GridAnimationConfig
import com.jayasuryat.minesweeperui.model.GridLayoutInformation
import com.jayasuryat.uigame.data.model.ToggleState
import com.jayasuryat.uigame.data.source.GameDataSource
import com.jayasuryat.uigame.data.source.GameSaver
import com.jayasuryat.uigame.feedback.sound.MusicManager
import com.jayasuryat.uigame.feedback.vibration.VibrationManager
import com.jayasuryat.uigame.logic.InitialGridProvider
import com.jayasuryat.uigame.logic.interactionlistener.ActionListener
import com.jayasuryat.uigame.logic.interactionlistener.from
import com.jayasuryat.uigame.logic.model.GameConfiguration
import com.jayasuryat.uigame.logic.model.GameScreenStatus
import com.jayasuryat.uigame.logic.model.GameScreenStatus.Loading
import com.jayasuryat.uigame.logic.model.GameState
import kotlinx.coroutines.*

class GameViewModel(
    internal val soundManager: MusicManager,
    internal val vibrationManager: VibrationManager,
    private val gameConfiguration: GameConfiguration,
    private val initialGridProvider: InitialGridProvider,
    private val minefieldController: MinefieldController,
    private val dataSource: GameDataSource,
) : ViewModel() {

    private val job: Job by lazy { SupervisorJob() }
    private val ioScope: CoroutineScope by lazy { CoroutineScope(Dispatchers.IO + job) }
    private val defaultScope: CoroutineScope by lazy { CoroutineScope(Dispatchers.Default + job) }

    private val _shouldShowToggle: MutableState<Boolean> = mutableStateOf(false)
    internal val shouldShowToggle: State<Boolean> = _shouldShowToggle

    private val _toggleState: MutableState<ToggleState> = mutableStateOf(ToggleState.Reveal)
    internal val toggleState: State<ToggleState> = _toggleState

    private val _screenStatus: MutableState<GameScreenStatus> = mutableStateOf(Loading)
    internal val screenStatus: State<GameScreenStatus> = _screenStatus

    private var actionListener: ActionListener? = null

    init {
        defaultScope.launch { loadGame() }
    }

    private suspend fun loadGame() {

        withContext(Dispatchers.Default) {

            val initialGrid = withContext(Dispatchers.IO) {
                initialGridProvider.getInitialGridFor(
                    rows = gameConfiguration.rows,
                    columns = gameConfiguration.columns,
                    totalMines = gameConfiguration.mines
                )
            }

            val actionListener = ActionListener(
                initialGrid = initialGrid,
                minefieldController = minefieldController,
                toggleState = toggleState,
                coroutineScope = defaultScope,
                musicManager = soundManager,
                vibrationManager = vibrationManager,
                onStateChanged = { saveCurrentGameState() },
            )

            val gridLayoutInformation = GridLayoutInformation.from(actionListener.statefulGrid)

            val size = gameConfiguration.rows * gameConfiguration.columns
            val animationConfig = GridAnimationConfig(
                enableCellAnimations = size <= DISABLE_ANIM_AFTER_SIZE,
            )

            val screenStatus = GameScreenStatus.Loaded(
                layoutInformation = gridLayoutInformation,
                interactionListener = actionListener,
                gameState = actionListener.gameState,
                gameProgress = actionListener.gameProgress,
                animationConfig = animationConfig,
            )

            this@GameViewModel.actionListener = actionListener

            withContext(Dispatchers.Main) {
                _screenStatus.value = screenStatus
                _toggleState.value = dataSource.getToggleState()
                _shouldShowToggle.value = dataSource.shouldShowToggle()
            }
        }
    }

    internal fun onToggleStateUpdated(newState: ToggleState) {
        _toggleState.value = newState
        ioScope.launch {
            dataSource.onToggleStateChanged(newState)
        }
    }

    internal fun saveCurrentGameState() {

        val grid = actionListener?.statefulGrid?.getCurrentGrid() ?: return

        val gameState = when (val status = _screenStatus.value) {
            is Loading -> return
            is GameScreenStatus.Loaded -> status
        }.gameState.value

        val mappedState = when (gameState) {
            is GameState.Idle -> return
            is GameState.GameStarted -> GameSaver.GameState.Started
            is GameState.GameEnded -> GameSaver.GameState.Ended
        }

        val startTime = if (gameState is GameState.StartedGameState) gameState.startTime else return
        val endTime = if (gameState is GameState.EndedGameState) gameState.endTime else null

        val elapsedMills = (endTime ?: System.currentTimeMillis()) - startTime

        dataSource.saveGame(
            grid = grid,
            elapsedDuration = elapsedMills / 1000,
            gameState = mappedState,
        )
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }

    companion object {

        private const val DISABLE_ANIM_AFTER_SIZE: Int = 300
    }
}

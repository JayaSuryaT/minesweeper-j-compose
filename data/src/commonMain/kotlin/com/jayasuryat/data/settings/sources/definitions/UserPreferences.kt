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
package com.jayasuryat.data.settings.sources.definitions

public interface UserPreferences {

    public suspend fun getIsSoundEnabled(): Boolean
    public suspend fun setIsSoundEnabled(enabled: Boolean)

    public suspend fun getIsVibrationEnabled(): Boolean
    public suspend fun setIsVibrationEnabled(enabled: Boolean)

    public suspend fun getShouldShowToggle(): Boolean
    public suspend fun setShouldShowToggle(show: Boolean)

    public suspend fun getDefaultToggleMode(): String?
    public suspend fun setDefaultToggleMode(mode: String)
}

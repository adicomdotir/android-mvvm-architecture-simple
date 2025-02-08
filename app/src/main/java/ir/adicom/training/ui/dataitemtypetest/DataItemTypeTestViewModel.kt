/*
 * Copyright (C) 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ir.adicom.training.ui.dataitemtypetest

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ir.adicom.training.data.DataItemTypeTestRepository
import ir.adicom.training.ui.dataitemtypetest.DataItemTypeTestUiState.Error
import ir.adicom.training.ui.dataitemtypetest.DataItemTypeTestUiState.Loading
import ir.adicom.training.ui.dataitemtypetest.DataItemTypeTestUiState.Success
import javax.inject.Inject

@HiltViewModel
class DataItemTypeTestViewModel @Inject constructor(
    private val dataItemTypeTestRepository: DataItemTypeTestRepository
) : ViewModel() {

    val uiState: StateFlow<DataItemTypeTestUiState> = dataItemTypeTestRepository
        .dataItemTypeTests.map<List<String>, DataItemTypeTestUiState>(::Success)
        .catch { emit(Error(it)) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), Loading)

    fun addDataItemTypeTest(name: String) {
        viewModelScope.launch {
            dataItemTypeTestRepository.add(name)
        }
    }
}

sealed interface DataItemTypeTestUiState {
    object Loading : DataItemTypeTestUiState
    data class Error(val throwable: Throwable) : DataItemTypeTestUiState
    data class Success(val data: List<String>) : DataItemTypeTestUiState
}

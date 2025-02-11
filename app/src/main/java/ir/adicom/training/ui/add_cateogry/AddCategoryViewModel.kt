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

package ir.adicom.training.ui.add_cateogry

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.adicom.training.data.CategoryRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ir.adicom.training.data.DataItemTypeTestRepository
import ir.adicom.training.data.local.database.DataItemType
import ir.adicom.training.ui.dataitemtypetest.DataItemTypeTestUiState.Error
import ir.adicom.training.ui.dataitemtypetest.DataItemTypeTestUiState.Loading
import ir.adicom.training.ui.dataitemtypetest.DataItemTypeTestUiState.Success
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class AddCategoryTestViewModel @Inject constructor(
    private val repository: CategoryRepository
) : ViewModel() {
    private val _state = MutableStateFlow(AddCategoryUiState())
    val state: StateFlow<AddCategoryUiState> = _state

    fun addCategory(title: String, color: Int) {
        viewModelScope.launch {
            if (title.isEmpty()) {
                _state.emit(_state.value.copy(validate = true))
            } else {
                _state.emit(_state.value.copy(loading = true))
                repository.addCategory(title, color)
                _state.emit(_state.value.copy(loading = false, success = true))
            }
        }
    }
}

data class AddCategoryUiState(
    val loading: Boolean = false,
    val error: String? = null,
    val success: Boolean = false,
    val validate: Boolean = false,
)
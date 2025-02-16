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

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.adicom.training.data.CategoryRepository
import ir.adicom.training.data.local.database.Category
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddCategoryViewModel @Inject constructor(
    private val repository: CategoryRepository
) : ViewModel() {
    private val _state = MutableStateFlow(AddCategoryUiState())
    val state: StateFlow<AddCategoryUiState> = _state

    private val _toastMessage = MutableSharedFlow<String>()
    val toastMessage: SharedFlow<String> = _toastMessage

    fun addCategory(title: String, color: Int) {
        viewModelScope.launch {
            if (title.isEmpty()) {
                _toastMessage.emit("Title cannot be empty")
            } else {
                _state.emit(_state.value.copy(loading = true))
                repository.addCategory(title, color)
                _state.emit(_state.value.copy(loading = false, success = true))
            }
        }
    }

    fun getCategory(id: Int) {
        viewModelScope.launch {
            _state.emit(_state.value.copy(loading = true))
            val result = repository.getCategoryById(id)
            _state.emit(_state.value.copy(loading = false, category = result))
        }
    }

    fun updateCategory(category: Category) {
        Log.i("TAG", "updateCategory: ${category.uid}")
        viewModelScope.launch {
            if (category.name.isEmpty()) {
                _toastMessage.emit("Title cannot be empty")
            } else {
                _state.emit(_state.value.copy(loading = true))
                repository.updateCategory(category)
                _state.emit(_state.value.copy(loading = false, success = true, category = null))
            }
        }
    }
}

data class AddCategoryUiState(
    val loading: Boolean = false,
    val error: String? = null,
    val success: Boolean = false,
    val category: Category? = null
)
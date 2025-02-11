package ir.adicom.training.ui.cateogry_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.adicom.training.data.CategoryRepository
import ir.adicom.training.data.local.database.Category
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class CategoryListViewModel @Inject constructor(
    repository: CategoryRepository
) : ViewModel() {
    val uiState: StateFlow<CategoryListUiState> = repository
        .categories.map<List<Category>, CategoryListUiState>(CategoryListUiState::Success)
        .catch { emit(CategoryListUiState.Error(it)) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), CategoryListUiState.Loading)
}

sealed interface CategoryListUiState {
    data object Loading : CategoryListUiState
    data class Error(val throwable: Throwable) : CategoryListUiState
    data class Success(val data: List<Category>) : CategoryListUiState
}

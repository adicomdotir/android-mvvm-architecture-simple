package ir.adicom.training.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.adicom.training.data.CategoryRepository
import ir.adicom.training.data.ExpenseRepository
import ir.adicom.training.data.local.database.Category
import ir.adicom.training.data.local.database.DataItemType
import ir.adicom.training.data.local.database.ExpenseCategoryPair
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val expenseRepository: ExpenseRepository,
    private val categoryRepository: CategoryRepository,
) : ViewModel() {

    val uiState: MutableStateFlow<HomeUiState> =
        MutableStateFlow(HomeUiState.Success(HomeModel(expenses = listOf(), categories = listOf())))

    init {
        fetchData()
    }

    private fun fetchData() {
        viewModelScope.launch {
            uiState.emit(HomeUiState.Loading)
            val combinedFlow = expenseRepository.expenses.combine(categoryRepository.categories) { f, s ->
                Log.e("TAG", "fetchData: ${f.size}", )
                Log.e("TAG", "fetchData: ${s.size}", )
                HomeModel(
                    expenses = f,
                    categories = s
                )
            }
            combinedFlow.collect {
                uiState.emit(HomeUiState.Success(it))
            }
        }
    }

}

sealed interface HomeUiState {
    data object Loading : HomeUiState
    data class Error(val throwable: Throwable) : HomeUiState
    data class Success(val data: HomeModel) : HomeUiState
}

data class HomeModel(
    val expenses: List<ExpenseCategoryPair>,
    val categories: List<Category>
)
package ir.adicom.training.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.adicom.training.data.CategoryRepository
import ir.adicom.training.data.ExpenseRepository
import ir.adicom.training.data.local.database.Category
import ir.adicom.training.data.local.database.ExpenseCategoryPair
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
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
            val combinedFlow = expenseRepository.expenses.combine(categoryRepository.categories) { ex, cat ->
                val balance = ex.map { it.price }.reduce { acc, i -> acc + i }
                HomeModel(
                    expenses = ex,
                    categories = cat,
                    balance = balance,
                    monthExpense = 0
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
    val balance: Int = 0,
    val monthExpense: Int = 0,
    val categories: List<Category>
)
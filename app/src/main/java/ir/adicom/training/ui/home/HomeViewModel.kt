package ir.adicom.training.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.adicom.training.data.CategoryRepository
import ir.adicom.training.data.ExpenseRepository
import ir.adicom.training.data.local.database.Category
import ir.adicom.training.data.local.database.ExpenseCategoryPair
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import java.time.YearMonth
import java.time.ZoneId
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val expenseRepository: ExpenseRepository,
    private val categoryRepository: CategoryRepository,
) : ViewModel() {

    val uiState: StateFlow<HomeUiState> = combine(
        expenseRepository.expenses,
        categoryRepository.categories
    ) { expenses, categories ->
        val balance = expenses.sumOf { it.price }
        val monthExpense = calculateMonthExpense(expenses)

        HomeUiState.Success(
            HomeModel(
                expenses = expenses,
                categories = categories,
                balance = balance,
                monthExpense = monthExpense
            )
        )
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = HomeUiState.Loading
        )

    private fun calculateMonthExpense(expenses: List<ExpenseCategoryPair>): Int {
        val currentYearMonth = YearMonth.now()
        val zoneId = ZoneId.systemDefault() // Or specify a specific time zone
        return expenses
            .filter {
                val zonedDateTime = it.dateTime.toInstant().atZone(zoneId)
                val expenseYearMonth = YearMonth.from(zonedDateTime)
                expenseYearMonth == currentYearMonth
            }
            .sumOf { it.price }
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
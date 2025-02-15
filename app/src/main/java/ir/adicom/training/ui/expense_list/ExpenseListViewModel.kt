package ir.adicom.training.ui.expense_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.adicom.training.data.ExpenseRepository
import ir.adicom.training.data.local.database.Expense
import ir.adicom.training.data.local.database.ExpenseCategoryPair
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExpenseListViewModel @Inject constructor(
    private val repository: ExpenseRepository
) : ViewModel() {
    val uiState: StateFlow<ExpenseListUiState> = repository
        .expenses.map<List<ExpenseCategoryPair>, ExpenseListUiState>(ExpenseListUiState::Success)
        .catch { emit(ExpenseListUiState.Error(it)) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ExpenseListUiState.Loading)

    fun delete(id: Int) {
        viewModelScope.launch {
            repository.deleteExpense(id)
        }
    }
}

sealed interface ExpenseListUiState {
    data object Loading : ExpenseListUiState
    data class Error(val throwable: Throwable) : ExpenseListUiState
    data class Success(val data: List<ExpenseCategoryPair>) : ExpenseListUiState
}

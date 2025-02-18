package ir.adicom.training.ui.add_expense

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.adicom.training.data.CategoryRepository
import ir.adicom.training.data.ExpenseRepository
import ir.adicom.training.data.local.database.Category
import ir.adicom.training.data.local.database.Expense
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class AddExpenseViewModel @Inject constructor(
    private val repository: ExpenseRepository,
    private val categoryRepository: CategoryRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(AddExpenseUiState())
    val state: StateFlow<AddExpenseUiState> = _state

    private val _categories = MutableStateFlow<List<Category>>(listOf())
    val categories = _categories

    private val _toastMessage = MutableSharedFlow<String>()
    val toastMessage: SharedFlow<String> = _toastMessage

    init {
        getCategories()
    }

    fun addExpense(title: String, price: String, id: Int) {
        viewModelScope.launch {
            if (title.isEmpty()) {
                _toastMessage.emit("Title cannot be empty")
            } else {
                _state.emit(_state.value.copy(loading = true))
                repository.addExpense(title, price = price.toInt(), id = id, date = Date())
                _state.emit(_state.value.copy(loading = false, success = true))
            }
        }
    }

    fun getExpense(id: Int) {
        viewModelScope.launch {
            _state.emit(_state.value.copy(loading = true))
            val result = repository.getExpenseById(id)
            _state.emit(_state.value.copy(loading = false, expense = result))
        }
    }

    private fun getCategories() {
        viewModelScope.launch {
             categoryRepository.categories.collect {
                 _categories.value = it
            }
        }
    }

    fun updateExpense(expense: Expense) {
        viewModelScope.launch {
            _state.emit(_state.value.copy(loading = true))
            repository.updateExpense(expense)
            _state.emit(_state.value.copy(loading = false, success = true))
        }
    }
}

data class AddExpenseUiState(
    val loading: Boolean = false,
    val error: String? = null,
    val success: Boolean = false,
    val expense: Expense? = null
)
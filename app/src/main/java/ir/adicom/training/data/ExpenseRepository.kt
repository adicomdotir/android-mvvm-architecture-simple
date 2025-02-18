package ir.adicom.training.data

import ir.adicom.training.data.local.database.Expense
import ir.adicom.training.data.local.database.ExpenseCategoryPair
import ir.adicom.training.data.local.database.ExpenseDao
import kotlinx.coroutines.flow.Flow
import java.util.Date
import javax.inject.Inject

interface ExpenseRepository {
    val expenses: Flow<List<ExpenseCategoryPair>>
    suspend fun addExpense(name: String, price: Int, id: Int, date: Date)
    suspend fun deleteExpense(id: Int)
    suspend fun getExpenseById(id: Int): Expense
    suspend fun updateExpense(expense: Expense)
}

class DefaultExpenseRepository @Inject constructor(
    private val expenseDao: ExpenseDao
) : ExpenseRepository {
    override val expenses: Flow<List<ExpenseCategoryPair>> = expenseDao.getExpenses()

    override suspend fun addExpense(title: String, price: Int, id: Int, date: Date) {
        val item = Expense(title = title, price =  price, categoryId = id, dateTime = date, description = null)
        expenseDao.insertItem(item)
    }

    override suspend fun deleteExpense(id: Int) {
        expenseDao.deleteItem(id)
    }

    override suspend fun getExpenseById(id: Int): Expense {
        return expenseDao.getExpenseById(id)
    }

    override suspend fun updateExpense(expense: Expense) {
        expenseDao.updateItem(expense = expense)
    }
}

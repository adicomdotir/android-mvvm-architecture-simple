package ir.adicom.training.data

import ir.adicom.training.data.local.database.Expense
import ir.adicom.training.data.local.database.ExpenseDao
import kotlinx.coroutines.flow.Flow
import java.util.Date
import javax.inject.Inject

interface ExpenseRepository {
    val categories: Flow<List<Expense>>
    suspend fun addExpense(name: String, price: Int, id: Int, date: Date)
    suspend fun deleteExpense(item: Expense)
}

class DefaultExpenseRepository @Inject constructor(
    private val expenseDao: ExpenseDao
) : ExpenseRepository {
    override val categories: Flow<List<Expense>> = expenseDao.getExpenses()

    override suspend fun addExpense(title: String, price: Int, id: Int, date: Date) {
        val item = Expense(title = title, price =  price, categoryId = id, dateTime = date, description = null)
        expenseDao.insertItem(item)
    }

    override suspend fun deleteExpense(item: Expense) {
        expenseDao.deleteItem(item)
    }
}

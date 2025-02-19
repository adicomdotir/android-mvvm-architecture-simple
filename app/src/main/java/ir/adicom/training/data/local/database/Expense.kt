package ir.adicom.training.data.local.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import java.util.Date

@Entity(
    tableName = "expenses", foreignKeys = [
        ForeignKey(
            entity = Category::class,
            parentColumns = arrayOf("uid"),
            childColumns = arrayOf("categoryId"),
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.RESTRICT
        )]
)
data class Expense(
    @PrimaryKey(autoGenerate = true)
    val uid: Int = 0,
    val title: String,
    val price: Int,
    val description: String?,
    val dateTime: Date,
    val categoryId: Int

)

data class ExpenseCategoryPair(
    val uid: Int,
    val title: String,
    val price: Int,
    val description: String?,
    val dateTime: Date,
    val categoryName: String,
    val categoryColor: Int,
)

@Dao
interface ExpenseDao {
//    @Query("SELECT * FROM expenses ORDER BY uid DESC")
//    fun getExpenses(): Flow<List<Expense>>

    @Query("SELECT expenses.*, categories.name AS categoryName, categories.color as categoryColor FROM expenses INNER JOIN categories on expenses.categoryId == categories.uid ORDER BY uid DESC")
    fun getExpenses(): Flow<List<ExpenseCategoryPair>>

    @Insert
    suspend fun insertItem(item: Expense)

    @Query("DELETE FROM expenses WHERE uid == :id;")
    suspend fun deleteItem(id: Int)

    @Query("SELECT * FROM expenses WHERE uid = :id LIMIT 1")
    suspend fun getExpenseById(id: Int): Expense

    @Update
    suspend fun updateItem(expense: Expense)
}

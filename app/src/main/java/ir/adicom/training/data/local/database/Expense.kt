package ir.adicom.training.data.local.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import java.util.Date

@Entity(
    tableName = "expenses", foreignKeys = [
        ForeignKey(
            entity = Category::class,
            parentColumns = arrayOf("uid"),
            childColumns = arrayOf("categoryId"),
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        )]
)
data class Expense(
    val title: String,
    val price: Int,
    val description: String?,
    val dateTime: Date,
    val categoryId: Int

) {
    @PrimaryKey(autoGenerate = true)
    var uid: Int = 0
}

@Dao
interface ExpenseDao {
    @Query("SELECT * FROM expenses ORDER BY uid DESC")
    fun getExpenses(): Flow<List<Expense>>

    @Insert
    suspend fun insertItem(item: Expense)

    @Delete
    suspend fun deleteItem(item: Expense)
}

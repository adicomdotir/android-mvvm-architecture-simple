package ir.adicom.training.data

import android.util.Log
import ir.adicom.training.TAG
import ir.adicom.training.data.local.database.Category
import ir.adicom.training.data.local.database.CategoryDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface CategoryRepository {
    val categories: Flow<List<Category>>
    suspend fun addCategory(name: String, color: Int)
    suspend fun deleteCategory(item: Category)
    suspend fun getCategoryById(id: Int): Category
    suspend fun updateCategory(category: Category)
}

class DefaultCategoryRepository @Inject constructor(
    private val categoryDao: CategoryDao
) : CategoryRepository {
    override val categories: Flow<List<Category>> = categoryDao.getCategories()

    override suspend fun addCategory(title: String, color: Int) {
        val item = Category(0, title, color)
        categoryDao.insertItem(item)
    }

    override suspend fun deleteCategory(item: Category) {
        try {
            categoryDao.deleteItem(item)
        } catch (e: Exception) {
            Log.e(TAG, "deleteCategory: ${e.message}", )
        }
    }

    override suspend fun getCategoryById(id: Int): Category {
        return categoryDao.getCategoryById(id)
    }

    override suspend fun updateCategory(category: Category) {
        Log.e(TAG, "updateCategory: ${category.uid}", )
        categoryDao.updateItem(category)
    }
}

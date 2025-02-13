/*
 * Copyright (C) 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ir.adicom.training.data

import ir.adicom.training.data.local.database.Category
import ir.adicom.training.data.local.database.CategoryDao
import kotlinx.coroutines.flow.Flow
import ir.adicom.training.data.local.database.DataItemType
import ir.adicom.training.data.local.database.DataItemTypeTestDao
import javax.inject.Inject

interface CategoryRepository {
    val categories: Flow<List<Category>>
    suspend fun addCategory(name: String, color: Int)
    suspend fun deleteCategory(item: Category)
}

class DefaultCategoryRepository @Inject constructor(
    private val categoryDao: CategoryDao
) : CategoryRepository {
    override val categories: Flow<List<Category>> = categoryDao.getCategories()

    override suspend fun addCategory(title: String, color: Int) {
        val item = Category(title, color)
        categoryDao.insertItem(item)
    }

    override suspend fun deleteCategory(item: Category) {
        categoryDao.deleteItem(item)
    }
}

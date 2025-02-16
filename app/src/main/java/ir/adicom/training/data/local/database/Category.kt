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

package ir.adicom.training.data.local.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "categories")
data class Category(
    @PrimaryKey(autoGenerate = true)
    val uid: Int = 0,
    val name: String,
    val color: Int
)

@Dao
interface CategoryDao {
    @Query("SELECT * FROM categories ORDER BY uid DESC")
    fun getCategories(): Flow<List<Category>>

    @Insert
    suspend fun insertItem(item: Category)

    @Delete
    suspend fun deleteItem(item: Category)

    @Query("SELECT * FROM categories WHERE uid = :id LIMIT 1")
    suspend fun getCategoryById(id: Int): Category

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateItem(item: Category)
}
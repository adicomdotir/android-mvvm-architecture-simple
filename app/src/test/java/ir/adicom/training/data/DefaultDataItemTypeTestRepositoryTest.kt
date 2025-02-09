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

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import ir.adicom.training.data.local.database.DataItemType
import ir.adicom.training.data.local.database.DataItemTypeTestDao

/**
 * Unit tests for [DefaultDataItemTypeTestRepository].
 */
@OptIn(ExperimentalCoroutinesApi::class) // TODO: Remove when stable
class DefaultDataItemTypeTestRepositoryTest {

    @Test
    fun dataItemTypeTests_newItemSaved_itemIsReturned() = runTest {
        val repository = DefaultDataItemTypeTestRepository(FakeDataItemTypeTestDao())

        repository.add("Repository")

        assertEquals(repository.dataItemTypeTests.first().size, 1)
    }

}

private class FakeDataItemTypeTestDao : DataItemTypeTestDao {

    private val data = mutableListOf<DataItemType>()

    override fun getDataItemTypeTests(): Flow<List<DataItemType>> = flow {
        emit(data)
    }

    override suspend fun insertDataItemTypeTest(item: DataItemType) {
        data.add(0, item)
    }
}

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

package ir.adicom.training.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ir.adicom.training.data.DataItemTypeTestRepository
import ir.adicom.training.data.DefaultDataItemTypeTestRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

    @Singleton
    @Binds
    fun bindsDataItemTypeTestRepository(
        dataItemTypeTestRepository: DefaultDataItemTypeTestRepository
    ): DataItemTypeTestRepository
}

//class FakeDataItemTypeTestRepository @Inject constructor() : DataItemTypeTestRepository {
//    override val dataItemTypeTests: Flow<List<DataItemTypeTest>> = flowOf(fakeDataItemTypeTests)
//
//    override suspend fun add(name: String) {
//        throw NotImplementedError()
//    }
//}
//
//val fakeDataItemTypeTests = listOf("One", "Two", "Three")

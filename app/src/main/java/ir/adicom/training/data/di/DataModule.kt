package ir.adicom.training.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ir.adicom.training.data.CategoryRepository
import ir.adicom.training.data.DataItemTypeTestRepository
import ir.adicom.training.data.DefaultCategoryRepository
import ir.adicom.training.data.DefaultDataItemTypeTestRepository
import ir.adicom.training.data.DefaultExpenseRepository
import ir.adicom.training.data.ExpenseRepository
import ir.adicom.training.data.local.database.DataItemType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

    @Singleton
    @Binds
    fun bindsDataItemTypeTestRepository(
        dataItemTypeTestRepository: DefaultDataItemTypeTestRepository
    ): DataItemTypeTestRepository

    @Singleton
    @Binds
    fun bindsCategoryRepository(
        categoryRepository: DefaultCategoryRepository
    ): CategoryRepository

    @Singleton
    @Binds
    fun bindsExpenseRepository(
        expenseRepository: DefaultExpenseRepository
    ): ExpenseRepository
}

class FakeDataItemTypeTestRepository @Inject constructor() : DataItemTypeTestRepository {
    override val dataItemTypeTests: Flow<List<DataItemType>> = flowOf(fakeDataItemTypeTests)

    override suspend fun add(name: String) {
        throw NotImplementedError()
    }

    override suspend fun delete(item: DataItemType) {
        throw NotImplementedError()
    }
}

val fakeDataItemTypeTests = listOf(
    DataItemType("One"),
    DataItemType("Two"),
    DataItemType("Three"),
)

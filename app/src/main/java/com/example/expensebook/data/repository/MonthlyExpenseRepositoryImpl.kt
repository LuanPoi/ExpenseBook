package com.example.expensebook.data.repository

import com.example.expensebook.data.data_source.local.LocalDatabase
import com.example.expensebook.data.data_source.local.dao.MonthlyExpenseDao
import com.example.expensebook.data.data_source.local.entities.MonthlyExpense
import com.example.expensebook.domain.repository.MonthlyExpenseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.time.YearMonth
import javax.inject.Inject

class MonthlyExpenseRepositoryImpl @Inject constructor(localDatabase: LocalDatabase) : MonthlyExpenseRepository {

    private val dao: MonthlyExpenseDao

    init {
        this.dao = localDatabase.monthlyExpenseDao()
    }

    override suspend fun insert(monthlyExpense: MonthlyExpense) {
        withContext(Dispatchers.IO) {
            dao.insert(monthlyExpense)
        }
    }

    override fun getAll(): Flow<List<MonthlyExpense>> {
        return dao.getAllWithFilter()
    }

    override fun getByDate(date: YearMonth): Flow<MonthlyExpense> {
        return dao.getByDate(date)
    }

    override suspend fun update(monthlyExpense: MonthlyExpense) {
        withContext(Dispatchers.IO) {
            dao.update(monthlyExpense)
        }
    }

    override suspend fun delete(monthlyExpense: MonthlyExpense) {
        withContext(Dispatchers.IO) {
            dao.delete(monthlyExpense)
        }
    }
}
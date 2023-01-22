package com.example.expensebook.data.repository

import android.app.Application
import com.example.expensebook.data.data_source.local.LocalDatabase
import com.example.expensebook.data.data_source.local.dao.MonthlyExpenseDao
import com.example.expensebook.data.model.entity.MonthlyExpense
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.time.YearMonth

class MonthlyExpenseRepository(application: Application) {

    private val dao: MonthlyExpenseDao

    init {
        this.dao = LocalDatabase.getDatabase(application).monthlyExpenseDao()
    }

    suspend fun addMonthlyExpense(monthlyExpense: MonthlyExpense){
        withContext(Dispatchers.IO){
            dao.insert(monthlyExpense)
        }
    }

    fun getAllMonthlyExpenses(): Flow<List<MonthlyExpense>>{
        return dao.getAllWithFilter()
    }

    fun getMonthlyExpenseByDate(date: YearMonth): Flow<MonthlyExpense> {
        return dao.getByDate(date)
    }

    fun getMonthlyExpenseByDateDistinctUntilChanged(date: YearMonth): Flow<MonthlyExpense> {
        return dao.getByDateDistinctUntilChanged(date)
    }

    suspend fun updateMonthlyExpense(monthlyExpense: MonthlyExpense){
        withContext(Dispatchers.IO){
            dao.update(monthlyExpense)
        }
    }

    suspend fun deleteMonthlyExpense(monthlyExpense: MonthlyExpense){
        withContext(Dispatchers.IO){
            dao.delete(monthlyExpense)
        }
    }
}
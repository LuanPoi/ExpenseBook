package com.example.expensebook.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.expensebook.data.LocalDatabase
import com.example.expensebook.data.dao.MonthlyExpenseDao
import com.example.expensebook.model.entity.MonthlyExpense
import kotlinx.coroutines.Dispatchers
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

    fun getAllMonthlyExpenses(): LiveData<List<MonthlyExpense>>{
        return dao.getAllWithFilter()
    }

    fun getMonthlyExpenseByDate(date: YearMonth): LiveData<MonthlyExpense>{
        return dao.getByDate(date)
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
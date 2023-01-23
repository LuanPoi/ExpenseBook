package com.example.expensebook.domain.repository

import com.example.expensebook.domain.model.MonthlyExpense
import kotlinx.coroutines.flow.Flow
import java.time.YearMonth

interface MonthlyExpenseRepository {
    suspend fun insert(monthlyExpense: MonthlyExpense)
    fun getAll(): Flow<List<MonthlyExpense>>
    fun getByDate(date: YearMonth): Flow<MonthlyExpense>
    suspend fun update(monthlyExpense: MonthlyExpense)
    suspend fun delete(monthlyExpense: MonthlyExpense)
}
package com.thepoi.expensebook.domain.repository

import com.thepoi.expensebook.data.data_source.local.entities.MonthlyExpense
import com.thepoi.expensebook.domain.model.filter.MonthlyExpenseFilter
import kotlinx.coroutines.flow.Flow
import java.time.YearMonth

interface MonthlyExpenseRepository {
    suspend fun insert(monthlyExpense: MonthlyExpense)
    fun getAllWithFilter(filter: MonthlyExpenseFilter): Flow<List<MonthlyExpense>>
    fun getByDate(date: YearMonth): Flow<MonthlyExpense>
    suspend fun update(monthlyExpense: MonthlyExpense)
    suspend fun delete(monthlyExpense: MonthlyExpense)
    fun getAllMonthlyExpenseDates(): Flow<List<YearMonth>>
}
package com.example.expensebook.domain.usecase

import com.example.expensebook.domain.model.MonthData
import com.example.expensebook.domain.repository.EntryRepository
import com.example.expensebook.domain.repository.MonthlyExpenseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import java.time.YearMonth

class FetchMonthDataUseCase(
    private val fetchMonthEntriesUseCase: FetchMonthEntriesUseCase,
    private val getMonthlyExpenseUseCase: GetMonthlyExpenseUseCase
) {
    suspend operator fun invoke(date: YearMonth): Flow<MonthData> {
        return combine(
            getMonthlyExpenseUseCase.invoke(date),
            fetchMonthEntriesUseCase.invoke(date)
        ){ monthlyExpense, monthEntries ->
            MonthData(monthlyExpense.date, monthlyExpense.initial_value, monthlyExpense.savings_goal, monthEntries)
        }
    }
}
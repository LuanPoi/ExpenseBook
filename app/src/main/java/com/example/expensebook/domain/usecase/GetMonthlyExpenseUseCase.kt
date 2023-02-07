package com.example.expensebook.domain.usecase

import com.example.expensebook.data.data_source.local.entities.MonthlyExpense
import com.example.expensebook.domain.repository.MonthlyExpenseRepository
import kotlinx.coroutines.flow.Flow
import java.time.YearMonth

class GetMonthlyExpenseUseCase(
    private val monthlyExpenseRepository: MonthlyExpenseRepository
) {
    suspend operator fun invoke(date: YearMonth): Flow<MonthlyExpense> {
        return monthlyExpenseRepository.getByDate(date)
    }
}
package com.thepoi.expensebook.domain.usecase

import com.thepoi.expensebook.data.data_source.local.entities.MonthlyExpense
import com.thepoi.expensebook.domain.repository.MonthlyExpenseRepository
import kotlinx.coroutines.flow.Flow
import java.time.YearMonth
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetMonthlyExpenseUseCase @Inject constructor(
    private val monthlyExpenseRepository: MonthlyExpenseRepository
) {
    operator fun invoke(date: YearMonth): Flow<MonthlyExpense?> {
        return monthlyExpenseRepository.getByDate(date)
    }
}
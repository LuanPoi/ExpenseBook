package com.thepoi.expensebook.domain.usecase

import com.thepoi.expensebook.domain.repository.MonthlyExpenseRepository
import kotlinx.coroutines.flow.Flow
import java.time.YearMonth
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetAllMonthlyExpenseDatesUseCase @Inject constructor(
    private val monthlyExpenseRepository: MonthlyExpenseRepository
) {
    operator fun invoke(): Flow<List<YearMonth>> {
        return monthlyExpenseRepository.getAllMonthlyExpenseDates()
    }
}
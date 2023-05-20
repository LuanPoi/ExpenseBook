package com.thepoi.expensebook.domain.usecase

import com.thepoi.expensebook.data.data_source.local.entities.MonthlyExpense
import com.thepoi.expensebook.domain.repository.MonthlyExpenseRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class GetMostRecentMonthlyExpenseUseCase @Inject constructor(
    private val monthlyExpenseRepository: MonthlyExpenseRepository
) {
    operator fun invoke(): Flow<MonthlyExpense?> {
        return monthlyExpenseRepository.getMostRecent()
    }
}
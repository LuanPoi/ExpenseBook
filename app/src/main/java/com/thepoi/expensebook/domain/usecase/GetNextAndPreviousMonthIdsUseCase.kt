package com.thepoi.expensebook.domain.usecase

import com.thepoi.expensebook.domain.repository.MonthlyExpenseRepository
import kotlinx.coroutines.flow.Flow
import java.time.YearMonth
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetNextAndPreviousMonthIdsUseCase @Inject constructor(
    private val monthlyExpenseRepository: MonthlyExpenseRepository
){
    suspend operator fun invoke(id: YearMonth): Flow<Pair<YearMonth?, YearMonth?>> {
        return monthlyExpenseRepository.getNextAndPreviousMonthIds(id)
    }
}
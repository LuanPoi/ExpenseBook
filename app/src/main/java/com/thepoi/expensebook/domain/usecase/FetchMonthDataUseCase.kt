package com.thepoi.expensebook.domain.usecase

import com.thepoi.expensebook.domain.model.MonthData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import java.time.YearMonth
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FetchMonthDataUseCase @Inject constructor(
    private val fetchMonthEntriesUseCase: FetchMonthEntriesUseCase,
    private val getMonthlyExpenseUseCase: GetMonthlyExpenseUseCase
) {
    operator fun invoke(date: YearMonth): Flow<MonthData> {
        return combine(
            getMonthlyExpenseUseCase(date),
            fetchMonthEntriesUseCase(date)
        ){ monthlyExpense, monthEntries ->
            MonthData(
                monthlyExpense.date,
                monthlyExpense.initial_value,
                monthlyExpense.savings_goal,
                monthEntries
            )
        }.distinctUntilChanged()
    }
}
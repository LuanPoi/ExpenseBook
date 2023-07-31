package com.thepoi.expensebook.ui.fragments.home

import java.time.YearMonth

data class HomeUiState(
    val monthDataUiState: MonthDataUiState,
    val dayDataUiState: DayDataUiState? = null,
    val entriesHistoryUiState: List<EntryUiState>
){
    data class MonthDataUiState(
        val monthNameOrdinal: Int,
        val expend: String,
        val remaining: String,
        val percentageExpend: Int,
        val initialValue: String,
        val savingsGoal: String,
        val idOfPreviousMonthWithData: YearMonth?,
        val idOfNextMonthWithData: YearMonth?
    )

    data class DayDataUiState(
        val recommendedDailyExpense: String,
        val expendToday: String,
        val remainingToday: String
    )

    data class EntryUiState(
        val id: Long,
        val icon: String = "@drawable/ic_money",
        val description: String,
        val value: String,
        val date: String
    )
}
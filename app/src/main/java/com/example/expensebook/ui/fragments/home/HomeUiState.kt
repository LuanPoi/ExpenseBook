package com.example.expensebook.ui.fragments.home

import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import com.example.expensebook.data.data_source.local.entities.Entry
import com.example.expensebook.data.data_source.local.entities.MonthlyExpense
import com.example.expensebook.data.data_source.local.entities.RecurringEntry
import java.time.YearMonth
import java.time.ZoneId

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
        val savingGoal: String
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
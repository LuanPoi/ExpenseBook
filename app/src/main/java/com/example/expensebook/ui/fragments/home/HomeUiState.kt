package com.example.expensebook.ui.fragments.home

import com.example.expensebook.data.data_source.local.entities.Entry
import com.example.expensebook.data.data_source.local.entities.MonthlyExpense
import com.example.expensebook.data.data_source.local.entities.RecurringEntry
import java.time.YearMonth
import java.time.ZoneId

data class HomeUiState(
    var currentYearMonth: YearMonth = YearMonth.now(ZoneId.systemDefault()),
    val currentMonthlyExpense: MonthlyExpense,
    val currentEntries: List<Entry>,
    val currentRecurringEntries: List<RecurringEntry>
)
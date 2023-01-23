package com.example.expensebook.ui.fragments.home

import com.example.expensebook.domain.model.Entry
import com.example.expensebook.domain.model.MonthlyExpense
import java.time.YearMonth
import java.time.ZoneId

data class HomeUiState(
    var currentYearMonth: YearMonth = YearMonth.now(ZoneId.systemDefault()),
    val currentMonthlyExpense: MonthlyExpense,
    val currentEntries: List<Entry>
)
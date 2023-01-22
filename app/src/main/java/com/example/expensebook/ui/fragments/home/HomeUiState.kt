package com.example.expensebook.ui.fragments.home

import com.example.expensebook.data.model.entity.Entry
import com.example.expensebook.data.model.entity.MonthlyExpense
import java.time.YearMonth
import java.time.ZoneId

data class HomeUiState(
    var currentYearMonth: YearMonth = YearMonth.now(ZoneId.systemDefault()),
    val currentMonthlyExpense: MonthlyExpense,
    val currentEntries: List<Entry>
)
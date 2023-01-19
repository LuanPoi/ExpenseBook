package com.example.expensebook.view.fragments.home

import com.example.expensebook.model.entity.Entry
import com.example.expensebook.model.entity.MonthlyExpense

data class HomeUiState(
    var currentMonthlyExpense: MonthlyExpense,
    var currentEntries: List<Entry>
)
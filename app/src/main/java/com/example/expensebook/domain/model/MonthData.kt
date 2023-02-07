package com.example.expensebook.domain.model

import com.example.expensebook.data.data_source.local.entities.Entry
import java.time.YearMonth

data class MonthData(
    var date: YearMonth,
    var initialValue: Float,
    var savingsGoal: Float,
    var totalExpend: Float,
    var remainingAmount: Float,
    var remainingDaysUntilEndOfMonth: Int,
    var entries: List<Entry>,
    var isCurrentMonth: Boolean,
)

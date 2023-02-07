package com.example.expensebook.domain.model

import java.time.LocalDate

data class DayData(
    var date: LocalDate,
    var recommendedExpendValue: Float,
    var expendToday: Float,
    var remainingToday: Float
)

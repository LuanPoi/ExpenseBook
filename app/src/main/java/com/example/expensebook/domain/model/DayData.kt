package com.example.expensebook.domain.model

import com.example.expensebook.data.data_source.local.entities.Entry
import java.time.LocalDate

data class DayData(
    val recommendedExpendValue: Float,
    val expendToday: Float
) {
    val remainingToday: Float
        get() = recommendedExpendValue + expendToday
}

package com.thepoi.expensebook.domain.model

data class DayData(
    val recommendedExpendValue: Float,
    val expendToday: Float
) {
    val remainingToday: Float
        get() = recommendedExpendValue + expendToday
}

package com.example.expensebook.domain.model

import com.example.expensebook.data.data_source.local.entities.Entry
import java.time.LocalDate
import java.time.YearMonth
import java.time.ZoneId

data class MonthData(
    val date: YearMonth,
    val initialValue: Float,
    val savingsGoal: Float,
    val entries: List<Entry>
){
    val totalExpend: Float
        get() = entries.map { it.value }.sum()
    val remainingAmount: Float
        get() = initialValue - savingsGoal + totalExpend
    val remainingDaysUntilEndOfMonth: Int
        get() = (date.plusMonths(1).atDay(1).toEpochDay() - LocalDate.now().toEpochDay()).toInt()
    val isCurrentMonth: Boolean
        get() = date.compareTo(YearMonth.now()) == 0
    val currentDayData: DayData?
        get() {
            if(!isCurrentMonth) return null
            return DayData(
                (initialValue - savingsGoal + totalExpendExceptDate(LocalDate.now())) / remainingDaysUntilEndOfMonth,
                entries.filter { entry -> entry.date.toLocalDate().isEqual(LocalDate.now()) }.map { entry -> entry.value }.sum()
            )
        }

    private fun totalExpendExceptDate(date: LocalDate): Float {
        return entries
            .filter { entry -> !entry.date.toLocalDate().isEqual(date) }
            .map { entry -> entry.value }
            .sum()
    }
}

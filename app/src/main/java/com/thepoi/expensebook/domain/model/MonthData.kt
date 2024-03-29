package com.thepoi.expensebook.domain.model

import com.thepoi.expensebook.data.data_source.local.entities.Entry
import java.time.LocalDate
import java.time.YearMonth

data class MonthData(
    val date: YearMonth,
    val initialValue: Float,
    val savingsGoal: Float,
    val entries: List<Entry>
){
    val totalExpend: Float
        get() = entries.map { it.amount }.sum()
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
                ((initialValue - savingsGoal + totalExpendExceptDate(LocalDate.now())) / remainingDaysUntilEndOfMonth),
                entries.filter { entry -> entry.datetime.toLocalDate().isEqual(LocalDate.now()) }.map { entry -> entry.amount }.sum()
            )
        }

    private fun totalExpendExceptDate(date: LocalDate): Float {
        return entries
            .filter { entry -> !entry.datetime.toLocalDate().isEqual(date) }
            .map { entry -> entry.amount }
            .sum()
    }
}

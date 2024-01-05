package com.thepoi.expensebook.data.data_source.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.YearMonth
import java.time.ZoneId

@Entity(tableName = "monthly_expense")
data class MonthlyExpense (
    @PrimaryKey
    var yearMonth: YearMonth = YearMonth.now(ZoneId.systemDefault()),
    var initialValue: Float = 0f,
    var savingsGoal: Float = 0f
)
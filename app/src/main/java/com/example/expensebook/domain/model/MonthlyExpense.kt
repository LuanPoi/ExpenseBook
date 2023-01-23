package com.example.expensebook.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.YearMonth
import java.time.ZoneId

@Entity(tableName = "monthly_expense")
data class MonthlyExpense (
    @PrimaryKey
    var date: YearMonth = YearMonth.now(ZoneId.systemDefault()),
    var initial_value: Float = 0f,
    var fixed_income: Float = 0f,
    var fixed_expense: Float = 0f,
    var savings_goal: Float = 0f
)
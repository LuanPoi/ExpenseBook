package com.example.expensebook.data.dao

import androidx.room.*
import com.example.expensebook.model.entity.MonthlyExpense

@Dao
interface MonthlyExpenseDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addMonthlyExpense(expense: MonthlyExpense)

    @Query("SELECT * FROM monthly_expense ORDER BY date ASC")
    fun getAllMonthlyExpenses(): List<MonthlyExpense>

    @Query("SELECT * FROM monthly_expense WHERE uid = :monthlyExpenseId")
    fun getMonthlyExpenseById(monthlyExpenseId: Int): MonthlyExpense

    @Update
    fun updateMonthlyExpense(monthlyExpense: MonthlyExpense)

    @Delete
    fun deleteMonthlyExpense(monthlyExpense: MonthlyExpense)
}
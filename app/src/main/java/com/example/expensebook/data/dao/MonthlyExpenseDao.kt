package com.example.expensebook.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.expensebook.model.entity.MonthlyExpense
import java.time.YearMonth

@Dao
interface MonthlyExpenseDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(expense: MonthlyExpense): Long

    @Query("SELECT * FROM monthly_expense " +
            "ORDER BY date ASC")
    fun getAllWithFilter(): LiveData<List<MonthlyExpense>>

    @Query("SELECT * FROM monthly_expense WHERE date = :date")
    fun getByDate(date: YearMonth): LiveData<MonthlyExpense>

    @Update
    fun update(monthlyExpense: MonthlyExpense): Int

    @Delete
    fun delete(monthlyExpense: MonthlyExpense): Int
}
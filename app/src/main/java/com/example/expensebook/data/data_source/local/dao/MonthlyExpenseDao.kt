package com.example.expensebook.data.data_source.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.expensebook.data.model.entity.MonthlyExpense
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import java.time.YearMonth

@Dao
abstract class MonthlyExpenseDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract fun insert(expense: MonthlyExpense): Long

    @Query("SELECT * FROM monthly_expense " +
            "ORDER BY date ASC")
    abstract fun getAllWithFilter(): Flow<List<MonthlyExpense>>

    @Query("SELECT * FROM monthly_expense WHERE date = :date")
    abstract fun getByDate(date: YearMonth): Flow<MonthlyExpense>

    fun getByDateDistinctUntilChanged(date: YearMonth): Flow<MonthlyExpense> = getByDate(date).distinctUntilChanged()

    @Update
    abstract fun update(monthlyExpense: MonthlyExpense): Int

    @Delete
    abstract fun delete(monthlyExpense: MonthlyExpense): Int
}
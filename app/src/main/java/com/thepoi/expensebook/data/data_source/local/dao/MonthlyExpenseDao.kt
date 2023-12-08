package com.thepoi.expensebook.data.data_source.local.dao

import androidx.room.*
import com.thepoi.expensebook.data.data_source.local.entities.MonthlyExpense
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import java.time.YearMonth

@Dao
abstract class MonthlyExpenseDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract fun insert(expense: MonthlyExpense): Long

    @Query("SELECT * FROM monthly_expense " +
            "WHERE ((:startYearMonth IS NULL OR date >= :startYearMonth) " +
            "AND (:endYearMonth IS NULL OR date <= :endYearMonth)" +
            ") ORDER BY date ASC")
    abstract fun getAllWithFilter(startYearMonth: YearMonth?, endYearMonth: YearMonth?): Flow<List<MonthlyExpense>>

    @Query("SELECT * FROM monthly_expense WHERE date = :date")
    abstract fun _getByDate(date: YearMonth): Flow<MonthlyExpense?>
    fun getByDate(date: YearMonth): Flow<MonthlyExpense?> = _getByDate(date).distinctUntilChanged()

    @Query("SELECT * FROM monthly_expense ORDER BY date DESC LIMIT 1")
    abstract fun getMostRecent(): Flow<MonthlyExpense?>

    @Update
    abstract fun update(monthlyExpense: MonthlyExpense): Int

    @Delete
    abstract fun delete(monthlyExpense: MonthlyExpense): Int

    @Query("SELECT date FROM monthly_expense ORDER BY date ASC")
    abstract fun _getAllDates(): Flow<List<YearMonth>>
    fun getAllDates(): Flow<List<YearMonth>> = _getAllDates().distinctUntilChanged()

    @Query("SELECT date FROM monthly_expense WHERE date < :date ORDER BY date DESC LIMIT 1")
    abstract fun getNextMonthId(date: YearMonth): Flow<YearMonth?>

    @Query("SELECT date FROM monthly_expense WHERE date > :date ORDER BY date ASC LIMIT 1")
    abstract fun getPreviousMonthId(date: YearMonth): Flow<YearMonth?>
}
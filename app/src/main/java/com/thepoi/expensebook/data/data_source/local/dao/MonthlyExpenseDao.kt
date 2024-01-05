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
            "WHERE ((:startYearMonth IS NULL OR yearMonth >= :startYearMonth) " +
            "AND (:endYearMonth IS NULL OR yearMonth <= :endYearMonth)" +
            ") ORDER BY yearMonth ASC")
    abstract fun getAllWithFilter(startYearMonth: YearMonth?, endYearMonth: YearMonth?): Flow<List<MonthlyExpense>>

    @Query("SELECT * FROM monthly_expense WHERE yearMonth = :yearMonth")
    abstract fun _getByDate(yearMonth: YearMonth): Flow<MonthlyExpense?>
    fun getByDate(yearMonth: YearMonth): Flow<MonthlyExpense?> = _getByDate(yearMonth).distinctUntilChanged()

    @Query("SELECT * FROM monthly_expense ORDER BY yearMonth DESC LIMIT 1")
    abstract fun getMostRecent(): Flow<MonthlyExpense?>

    @Update
    abstract fun update(monthlyExpense: MonthlyExpense): Int

    @Delete
    abstract fun delete(monthlyExpense: MonthlyExpense): Int

    @Query("SELECT yearMonth FROM monthly_expense ORDER BY yearMonth ASC")
    abstract fun _getAllDates(): Flow<List<YearMonth>>
    fun getAllDates(): Flow<List<YearMonth>> = _getAllDates().distinctUntilChanged()

    @Query("SELECT yearMonth FROM monthly_expense WHERE yearMonth < :yearMonth ORDER BY yearMonth DESC LIMIT 1")
    abstract fun getNextMonthId(yearMonth: YearMonth): Flow<YearMonth?>

    @Query("SELECT yearMonth FROM monthly_expense WHERE yearMonth > :yearMonth ORDER BY yearMonth ASC LIMIT 1")
    abstract fun getPreviousMonthId(yearMonth: YearMonth): Flow<YearMonth?>
}
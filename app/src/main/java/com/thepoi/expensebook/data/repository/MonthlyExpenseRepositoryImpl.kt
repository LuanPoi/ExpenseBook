package com.thepoi.expensebook.data.repository

import com.thepoi.expensebook.data.data_source.local.LocalDatabase
import com.thepoi.expensebook.data.data_source.local.dao.MonthlyExpenseDao
import com.thepoi.expensebook.data.data_source.local.entities.MonthlyExpense
import com.thepoi.expensebook.domain.model.filter.MonthlyExpenseFilter
import com.thepoi.expensebook.domain.repository.MonthlyExpenseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.withContext
import java.time.YearMonth
import javax.inject.Inject

class MonthlyExpenseRepositoryImpl @Inject constructor(localDatabase: LocalDatabase) : MonthlyExpenseRepository {

    private val dao: MonthlyExpenseDao

    init {
        this.dao = localDatabase.monthlyExpenseDao()
    }

    override suspend fun insert(monthlyExpense: MonthlyExpense) {
        return withContext(Dispatchers.IO) {
            dao.insert(monthlyExpense)
        }
    }

    override fun getAllWithFilter(filter: MonthlyExpenseFilter): Flow<List<MonthlyExpense>> {
        return dao.getAllWithFilter(filter.startYearMonth, filter.endYearMonth)
    }

    override fun getByDate(date: YearMonth): Flow<MonthlyExpense?> {
        return dao.getByDate(date)
    }

    override fun getMostRecent(): Flow<MonthlyExpense?> {
        return dao.getMostRecent()
    }

    override suspend fun update(monthlyExpense: MonthlyExpense) {
        withContext(Dispatchers.IO) {
            dao.update(monthlyExpense)
        }
    }

    override suspend fun delete(monthlyExpense: MonthlyExpense) {
        withContext(Dispatchers.IO) {
            dao.delete(monthlyExpense)
        }
    }

    override fun getAllMonthlyExpenseDates(): Flow<List<YearMonth>> {
        return dao.getAllDates()
    }

    override fun getNextAndPreviousMonthIds(date: YearMonth): Flow<Pair<YearMonth?, YearMonth?>> {
        return combine(
            dao.getNextMonthId(date),
            dao.getPreviousMonthId(date)
        ){ previous, next ->
            Pair<YearMonth?, YearMonth?>(previous, next)
        }
    }

    override suspend fun createEmptyIfDontExist(date: YearMonth) {
        this.getByDate(date).collectLatest {
            if(it == null) {
                withContext(Dispatchers.IO) {
                    dao.insert(MonthlyExpense(yearMonth = date))
                }
            }
        }
    }
}
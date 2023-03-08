package com.thepoi.expensebook.data.repository

import com.thepoi.expensebook.data.data_source.local.LocalDatabase
import com.thepoi.expensebook.data.data_source.local.dao.RecurringEntryDao
import com.thepoi.expensebook.data.data_source.local.entities.RecurringEntry
import com.thepoi.expensebook.domain.model.filter.RecurringEntryFilter
import com.thepoi.expensebook.domain.repository.RecurringEntryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RecurringEntryRepositoryImpl @Inject constructor(localDatabase: LocalDatabase) : RecurringEntryRepository {

    private val dao: RecurringEntryDao

    init {
        this.dao = localDatabase.recurringEntryDao()
    }

    override suspend fun insert(recurringEntry: RecurringEntry) {
        withContext(Dispatchers.IO){
            dao.insert(recurringEntry)
        }
    }

    override fun getAllWithFilter(filter: RecurringEntryFilter): Flow<List<RecurringEntry>> {
        if(filter.getExpenses == false) filter.getExpenses = null
        if(filter.getIncomes == false) filter.getIncomes = null

        return dao.getAllWithFilter(filter.getExpenses, filter.getIncomes)
    }

    override fun getById(id: Long): Flow<RecurringEntry> {
        return dao.getById(id)
    }

    override suspend fun update(recurringEntry: RecurringEntry) {
        withContext(Dispatchers.IO){
            dao.update(recurringEntry)
        }
    }

    override suspend fun delete(recurringEntry: RecurringEntry) {
        withContext(Dispatchers.IO){
            dao.delete(recurringEntry)
        }
    }
}
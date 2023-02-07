package com.example.expensebook.data.repository

import android.app.Application
import com.example.expensebook.data.data_source.local.LocalDatabase
import com.example.expensebook.data.data_source.local.dao.RecurringEntryDao
import com.example.expensebook.data.data_source.local.entities.RecurringEntry
import com.example.expensebook.domain.repository.RecurringEntryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class RecurringEntryRepositoryImpl(application: Application) : RecurringEntryRepository {

    private val dao: RecurringEntryDao

    init {
        this.dao = LocalDatabase.getDatabase(application).recurringEntryDao()
    }

    override suspend fun insert(recurringEntry: RecurringEntry) {
        withContext(Dispatchers.IO){
            dao.insert(recurringEntry)
        }
    }

    override fun getAll(): Flow<List<RecurringEntry>> {
        return dao.getAll()
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
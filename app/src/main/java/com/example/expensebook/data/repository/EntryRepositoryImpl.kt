package com.example.expensebook.data.repository

import android.app.Application
import com.example.expensebook.data.data_source.local.LocalDatabase
import com.example.expensebook.data.data_source.local.dao.EntryDao
import com.example.expensebook.data.data_source.local.entities.Entry
import com.example.expensebook.domain.repository.EntryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.time.OffsetDateTime
import java.time.YearMonth
import java.time.ZoneOffset

class EntryRepositoryImpl(application: Application) : EntryRepository {

    private val dao: EntryDao

    init {
        this.dao = LocalDatabase.getDatabase(application).entryDao()
    }

    override suspend fun insert(entry: Entry){
        withContext(Dispatchers.IO){
            dao.insert(entry)
        }
    }

    override fun getAll(yearMonth: YearMonth): Flow<List<Entry>> {
        return dao.getAllWithFilter(
            OffsetDateTime.from(
                yearMonth.atDay(1).atStartOfDay().atZone(ZoneOffset.systemDefault())
            ),
            OffsetDateTime.from(
                yearMonth.plusMonths(1).atDay(1).atStartOfDay().atZone(ZoneOffset.systemDefault())
            )
        )
    }

    override fun getById(entryId: Long): Flow<Entry> {
        return dao.getById(entryId)
    }

    override suspend fun update(entry: Entry) {
        withContext(Dispatchers.IO){
            dao.update(entry)
        }
    }

    override suspend fun delete(entry: Entry) {
        withContext(Dispatchers.IO){
            dao.delete(entry)
        }
    }
}
package com.example.expensebook.data.repository

import android.app.Application
import com.example.expensebook.data.data_source.local.LocalDatabase
import com.example.expensebook.data.data_source.local.dao.EntryDao
import com.example.expensebook.data.model.entity.Entry
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.time.OffsetDateTime
import java.time.YearMonth
import java.time.ZoneOffset

class EntryRepository(application: Application) {

    private val dao: EntryDao

    init {
        this.dao = LocalDatabase.getDatabase(application).entryDao()
    }

    suspend fun addEntry(entry: Entry): Entry {
        return withContext(Dispatchers.IO){
            dao.insert(entry).let { entryId ->
                entry.apply { uid = entryId }
            }
        }
    }

    fun getEntries(yearMonth: YearMonth): Flow<List<Entry>> {
        return dao.getAllWithFilter(
            OffsetDateTime.from(
                yearMonth.atDay(1).atStartOfDay().atZone(ZoneOffset.systemDefault())
            ),
            OffsetDateTime.from(
                yearMonth.plusMonths(1).atDay(1).atStartOfDay().atZone(ZoneOffset.systemDefault())
            )
        )
    }

    fun getEntryById(entryId: Long): Flow<Entry> {
        return dao.getById(entryId)
    }

    fun getEntryByIdDistinctUntilChanged(entryId: Long): Flow<Entry> {
        return dao.getByIdDistinctUntilChanged(entryId)
    }

    suspend fun updateEntry(entry: Entry) = withContext(Dispatchers.IO){
        dao.update(entry)
    }

    suspend fun deleteEntry(entry: Entry) = withContext(Dispatchers.IO){
        dao.delete(entry)
    }
}
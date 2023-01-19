package com.example.expensebook.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.expensebook.data.LocalDatabase
import com.example.expensebook.data.dao.EntryDao
import com.example.expensebook.model.entity.Entry
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.OffsetDateTime
import java.time.YearMonth
import java.time.ZoneOffset

class EntryRepository(application: Application) {

    private val dao: EntryDao

    init {
        this.dao = LocalDatabase.getDatabase(application).entryDao()
    }

    suspend fun addEntry(entry: Entry): Entry{
        return withContext(Dispatchers.IO){
            dao.insert(entry).let { entryId ->
                entry.apply { uid = entryId }
            }
        }
    }

    fun getEntries(yearMonth: YearMonth): LiveData<List<Entry>> {
        return dao.getAllWithFilter(
            OffsetDateTime.from(
                yearMonth.atDay(1).atStartOfDay().atZone(ZoneOffset.systemDefault())
            ),
            OffsetDateTime.from(
                yearMonth.plusMonths(1).atDay(1).atStartOfDay().atZone(ZoneOffset.systemDefault())
            )
        )
    }

    fun getEntryById(entryId: Long): LiveData<Entry> {
        return dao.getById(entryId)
    }

    suspend fun updateEntry(entry: Entry) = withContext(Dispatchers.IO){
        dao.update(entry)
    }

    suspend fun deleteEntry(entry: Entry) = withContext(Dispatchers.IO){
        dao.delete(entry)
    }
}
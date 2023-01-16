package com.example.expensebook.repository

import android.app.Application
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import com.example.expensebook.data.LocalDatabase
import com.example.expensebook.data.dao.EntryDao
import com.example.expensebook.model.entity.Entry
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.invoke
import kotlinx.coroutines.withContext
import java.time.OffsetDateTime
import java.time.YearMonth
import java.time.ZoneOffset

class EntryRepository(application: Application) {

    private val entryDao: EntryDao

    init {
        this.entryDao = LocalDatabase.getDatabase(application).entryDao()
    }

    suspend fun addEntry(entry: Entry) = withContext(Dispatchers.IO){
        entryDao.addEntry(entry)
    }

    fun getEntries(yearMonth: YearMonth): LiveData<List<Entry>> {
        return entryDao.getEntries(
            OffsetDateTime.from(
                yearMonth.atDay(1).atStartOfDay().atZone(ZoneOffset.systemDefault())
            ),
            OffsetDateTime.from(
                yearMonth.atEndOfMonth().atTime(23, 59, 59).atZone(ZoneOffset.systemDefault())
            )
        )
    }

    fun getEntryById(entryId: Long): LiveData<Entry> {
        return entryDao.getEntryById(entryId)
    }

    suspend fun updateEntry(entry: Entry) = withContext(Dispatchers.IO){
        entryDao.updateEntry(entry)
    }

    suspend fun deleteEntry(entry: Entry) = withContext(Dispatchers.IO){
        entryDao.deleteEntry(entry)
    }
}
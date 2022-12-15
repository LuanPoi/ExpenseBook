package com.example.expensebook.repository

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.expensebook.MainActivity
import com.example.expensebook.data.LocalDatabase
import com.example.expensebook.data.dao.EntryDao
import com.example.expensebook.model.entity.Entry
import java.time.OffsetDateTime
import java.time.YearMonth
import java.time.ZoneId
import java.time.ZoneOffset

class EntryRepository(application: Application) {

    private val entryDao: EntryDao

    init {
        this.entryDao = LocalDatabase.getDatabase(application).entryDao()
    }

    fun addEntry(entry: Entry){
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

    fun updateEntry(entry: Entry){
        entryDao.updateEntry(entry)
    }

    fun deleteEntre(entry: Entry){
        entryDao.deleteEntry(entry)
    }
}
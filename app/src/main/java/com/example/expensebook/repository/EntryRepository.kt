package com.example.expensebook.repository

import androidx.lifecycle.LiveData
import com.example.expensebook.data.dao.EntryDao
import com.example.expensebook.model.entity.Entry

class EntryRepository(private val entryDao: EntryDao) {

    fun addEntry(entry: Entry){
        entryDao.addEntry(entry)
    }

    fun getAllEntries(): List<Entry> {
        return entryDao.getAllEntries()
    }

    fun getEntryById(entryId: Int): Entry {
        return entryDao.getEntryById(entryId)
    }

    fun updateEntry(entry: Entry){
        entryDao.updateEntry(entry)
    }

    fun deleteEntre(entry: Entry){
        entryDao.deleteEntry(entry)
    }
}
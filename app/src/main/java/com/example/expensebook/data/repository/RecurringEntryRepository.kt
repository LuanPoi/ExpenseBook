package com.example.expensebook.data.repository

import com.example.expensebook.data.data_source.local.dao.RecurringEntryDao
import com.example.expensebook.data.model.entity.RecurringEntry

class RecurringEntryRepository(private val recurringEntryDao: RecurringEntryDao) {

    fun addRecurringEntry(recurringEntry: RecurringEntry){
        recurringEntryDao.addRecurringEntry(recurringEntry)
    }

    fun getAllRecurringEntries(): List<RecurringEntry>{
        return recurringEntryDao.getAllRecurringEntries()
    }

    fun getRecurringEntryById(recurringEntryId: Int): RecurringEntry {
        return recurringEntryDao.getRecurringEntryById(recurringEntryId)
    }

    fun updateRecurringEntry(recurringEntry: RecurringEntry){
        recurringEntryDao.updateRecurringEntry(recurringEntry)
    }

    fun deleteRecurringEntry(recurringEntry: RecurringEntry){
        recurringEntryDao.deleteRecurringEntry(recurringEntry)
    }
}
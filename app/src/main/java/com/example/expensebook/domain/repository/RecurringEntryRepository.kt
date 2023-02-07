package com.example.expensebook.domain.repository

import com.example.expensebook.data.data_source.local.entities.RecurringEntry
import kotlinx.coroutines.flow.Flow

interface RecurringEntryRepository {
    suspend fun insert(recurringEntry: RecurringEntry)
    fun getAll(): Flow<List<RecurringEntry>>
    fun getById(id: Long): Flow<RecurringEntry>
    suspend fun update(recurringEntry: RecurringEntry)
    suspend fun delete(recurringEntry: RecurringEntry)
}
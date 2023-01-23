package com.example.expensebook.domain.repository

import com.example.expensebook.domain.model.Entry
import kotlinx.coroutines.flow.Flow
import java.time.YearMonth

interface EntryRepository {
    suspend fun insert(entry: Entry)
    fun getAll(yearMonth: YearMonth): Flow<List<Entry>>
    fun getById(entryId: Long): Flow<Entry>
    suspend fun update(entry: Entry)
    suspend fun delete(entry: Entry)
}
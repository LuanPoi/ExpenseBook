package com.example.expensebook.domain.repository

import com.example.expensebook.data.data_source.local.entities.Entry
import com.example.expensebook.domain.model.filter.EntryFilter
import kotlinx.coroutines.flow.Flow
import java.time.YearMonth

interface EntryRepository {
    suspend fun insert(entry: Entry)
    fun getAllWithFilter(filter: EntryFilter): Flow<List<Entry>>
    fun getById(entryId: Long): Flow<Entry>
    suspend fun update(entry: Entry)
    suspend fun delete(entry: Entry)
}
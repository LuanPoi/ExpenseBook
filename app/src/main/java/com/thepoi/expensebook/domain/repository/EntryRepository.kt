package com.thepoi.expensebook.domain.repository

import com.thepoi.expensebook.data.data_source.local.entities.Entry
import com.thepoi.expensebook.domain.model.filter.EntryFilter
import kotlinx.coroutines.flow.Flow

interface EntryRepository {
    suspend fun insert(entry: Entry)
    fun getAllWithFilter(filter: EntryFilter): Flow<List<Entry>>
    fun getById(entryId: Long): Flow<Entry>
    suspend fun update(entry: Entry)
    suspend fun delete(entry: Entry)
}
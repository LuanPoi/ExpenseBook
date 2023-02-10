package com.example.expensebook.data.repository

import com.example.expensebook.data.data_source.local.LocalDatabase
import com.example.expensebook.data.data_source.local.dao.EntryDao
import com.example.expensebook.data.data_source.local.entities.Entry
import com.example.expensebook.domain.model.filter.EntryFilter
import com.example.expensebook.domain.repository.EntryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.time.OffsetDateTime
import java.time.YearMonth
import java.time.ZoneOffset
import javax.inject.Inject

class EntryRepositoryImpl @Inject constructor(localDatabase: LocalDatabase) : EntryRepository {

    private val dao: EntryDao

    init {
        this.dao = localDatabase.entryDao()
    }

    override suspend fun insert(entry: Entry){
        withContext(Dispatchers.IO){
            dao.insert(entry)
        }
    }

    override fun getAllWithFilter(filter: EntryFilter): Flow<List<Entry>> {
        return dao.getAllWithFilter(filter.startOffsetDateTime, filter.exclusiveEndOffsetDateTime)
    }

    override fun getById(entryId: Long): Flow<Entry> {
        return dao.getById(entryId)
    }

    override suspend fun update(entry: Entry) {
        withContext(Dispatchers.IO){
            dao.update(entry)
        }
    }

    override suspend fun delete(entry: Entry) {
        withContext(Dispatchers.IO){
            dao.delete(entry)
        }
    }
}
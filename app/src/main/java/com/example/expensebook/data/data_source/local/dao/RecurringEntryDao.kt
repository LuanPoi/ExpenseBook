package com.example.expensebook.data.data_source.local.dao

import androidx.room.*
import com.example.expensebook.data.data_source.local.entities.RecurringEntry
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged

@Dao
abstract class RecurringEntryDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun insert(recurringEntry: RecurringEntry)

    @Query("SELECT * FROM recurring_entry ORDER BY description ASC")
    abstract fun getAll(): Flow<List<RecurringEntry>>

    @Query("SELECT * FROM recurring_entry WHERE uid = :id")
    abstract fun _getById(id: Long): Flow<RecurringEntry>

    fun getById(id: Long): Flow<RecurringEntry> = _getById(id).distinctUntilChanged()

    @Update
    abstract suspend fun update(recurringEntry: RecurringEntry)

    @Delete
    abstract suspend fun delete(recurringEntry: RecurringEntry)
}
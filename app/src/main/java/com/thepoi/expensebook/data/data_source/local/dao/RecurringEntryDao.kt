package com.thepoi.expensebook.data.data_source.local.dao

import androidx.room.*
import com.thepoi.expensebook.data.data_source.local.entities.RecurringEntry
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged

@Dao
abstract class RecurringEntryDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun insert(recurringEntry: RecurringEntry)

    @Query("SELECT * FROM recurring_entry"
            +" WHERE ("
            +"(:getExpenses IS NULL OR value < 0)"
            +" AND "
            +"(:getIncomes IS NULL OR value >= 0)"
            +") ORDER BY description ASC")
    abstract fun getAllWithFilter(getExpenses: Boolean?, getIncomes: Boolean?): Flow<List<RecurringEntry>>

    @Query("SELECT * FROM recurring_entry WHERE uid = :id")
    abstract fun _getById(id: Long): Flow<RecurringEntry>

    fun getById(id: Long): Flow<RecurringEntry> = _getById(id).distinctUntilChanged()

    @Update
    abstract suspend fun update(recurringEntry: RecurringEntry)

    @Delete
    abstract suspend fun delete(recurringEntry: RecurringEntry)
}
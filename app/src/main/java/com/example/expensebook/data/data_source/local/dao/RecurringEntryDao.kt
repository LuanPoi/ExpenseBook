package com.example.expensebook.data.data_source.local.dao

import androidx.room.*
import com.example.expensebook.data.model.entity.Entry
import com.example.expensebook.data.model.entity.RecurringEntry

@Dao
interface RecurringEntryDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addRecurringEntry(recurringEntry: RecurringEntry)

    @Query("SELECT * FROM recurring_entry ORDER BY description ASC")
    fun getAllRecurringEntries(): List<RecurringEntry>

    @Query("SELECT * FROM recurring_entry WHERE uid = :recurringEntryId")
    fun getRecurringEntryById(recurringEntryId: Int): RecurringEntry

    @Update
    fun updateRecurringEntry(recurringEntry: RecurringEntry)

    @Delete
    fun deleteRecurringEntry(recurringEntry: RecurringEntry)
}
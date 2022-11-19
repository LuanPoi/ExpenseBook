package com.example.expensebook.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.expensebook.model.entity.Entry

@Dao
interface EntryDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addEntry(entry: Entry)

    @Query("SELECT * FROM entry ORDER BY date ASC")
    fun getAllEntries(): List<Entry>

    @Query("SELECT * FROM entry WHERE uid = :entryId")
    fun getEntryById(entryId: Int): Entry

    @Update
    fun updateEntry(entry: Entry)

    @Delete
    fun deleteEntry(entry: Entry)
}
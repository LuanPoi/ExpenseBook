package com.example.expensebook.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.expensebook.model.entity.Entry
import java.time.OffsetDateTime

@Dao
interface EntryDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addEntry(entry: Entry): Long

    @Query("SELECT * FROM entry " +
            "WHERE ((:startOffsetDateTime IS NULL OR date >= :startOffsetDateTime) " +
                "AND (:endOffsetDateTime IS NULL OR date <= :endOffsetDateTime)" +
            ") ORDER BY date ASC")
    fun getEntries(startOffsetDateTime: OffsetDateTime?, endOffsetDateTime: OffsetDateTime?): LiveData<List<Entry>>

    @Query("SELECT * FROM entry WHERE uid = :entryId")
    fun getEntryById(entryId: Long): LiveData<Entry>

    @Update
    fun updateEntry(entry: Entry): Int

    @Delete
    fun deleteEntry(entry: Entry): Int
}
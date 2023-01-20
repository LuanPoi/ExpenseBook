package com.example.expensebook.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.expensebook.model.entity.Entry
import java.time.OffsetDateTime

@Dao
interface EntryDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(entry: Entry): Long

    @Query("SELECT * FROM entry " +
            "WHERE ((:startOffsetDateTime IS NULL OR date >= :startOffsetDateTime) " +
                "AND (:endOffsetDateTime IS NULL OR date < :endOffsetDateTime)" +
            ") ORDER BY date ASC")
    fun getAllWithFilter(startOffsetDateTime: OffsetDateTime?, endOffsetDateTime: OffsetDateTime?): LiveData<List<Entry>>

    @Query("SELECT * FROM entry WHERE uid = :entryId")
    fun getById(entryId: Long): LiveData<Entry>

    @Update
    fun update(entry: Entry): Int

    @Delete
    fun delete(entry: Entry): Int
}
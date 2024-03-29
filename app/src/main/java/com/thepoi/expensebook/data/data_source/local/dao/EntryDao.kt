package com.thepoi.expensebook.data.data_source.local.dao

import androidx.room.*
import com.thepoi.expensebook.data.data_source.local.entities.Entry
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import java.time.OffsetDateTime
import java.time.ZonedDateTime

@Dao
abstract class EntryDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun insert(entry: Entry): Long

    @Query("SELECT * FROM entry " +
            "WHERE ((:startDateTime IS NULL OR datetime >= :startDateTime) " +
                "AND (:exclusiveEndDateTime IS NULL OR datetime < :exclusiveEndDateTime)" +
            ") ORDER BY datetime ASC")
    abstract fun getAllWithFilter(startDateTime: ZonedDateTime?, exclusiveEndDateTime: ZonedDateTime?): Flow<List<Entry>>

    @Query("SELECT * FROM entry WHERE id = :id")
    abstract fun _getById(id: Long): Flow<Entry?>
    fun getById(id: Long): Flow<Entry?> = _getById(id).distinctUntilChanged()

    @Update
    abstract suspend fun update(entry: Entry): Int

    @Delete
    abstract suspend fun delete(entry: Entry): Int
}
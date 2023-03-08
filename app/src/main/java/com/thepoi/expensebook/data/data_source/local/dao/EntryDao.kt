package com.thepoi.expensebook.data.data_source.local.dao

import androidx.room.*
import com.thepoi.expensebook.data.data_source.local.entities.Entry
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import java.time.OffsetDateTime

@Dao
abstract class EntryDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun insert(entry: Entry): Long

    @Query("SELECT * FROM entry " +
            "WHERE ((:startOffsetDateTime IS NULL OR date >= :startOffsetDateTime) " +
                "AND (:exclusiveEndOffsetDateTime IS NULL OR date < :exclusiveEndOffsetDateTime)" +
            ") ORDER BY date ASC")
    abstract fun getAllWithFilter(startOffsetDateTime: OffsetDateTime?, exclusiveEndOffsetDateTime: OffsetDateTime?): Flow<List<Entry>>

    @Query("SELECT * FROM entry WHERE uid = :id")
    abstract fun _getById(id: Long): Flow<Entry>

    fun getById(id: Long): Flow<Entry> = _getById(id).distinctUntilChanged()

    @Update
    abstract suspend fun update(entry: Entry): Int

    @Delete
    abstract suspend fun delete(entry: Entry): Int
}
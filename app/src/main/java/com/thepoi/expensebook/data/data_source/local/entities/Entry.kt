package com.thepoi.expensebook.data.data_source.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.ZoneId
import java.time.ZonedDateTime

@Entity(tableName = "entry")
data class Entry(
    @PrimaryKey(autoGenerate = true) var id: Long? = null,
    var datetime: ZonedDateTime = ZonedDateTime.now(ZoneId.systemDefault()),
    @ColumnInfo(typeAffinity = ColumnInfo.TEXT) var amount: Float = 0f,
    var description: String = ""
)

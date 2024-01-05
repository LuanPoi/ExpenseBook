package com.thepoi.expensebook.data.data_source.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.ZonedDateTime

@Entity(tableName = "entry")
data class Entry(
    @PrimaryKey(autoGenerate = true) var id: Long? = null,
    var datetime: ZonedDateTime = ZonedDateTime.now(ZoneId.systemDefault()),
    var amount: Float = 0f,
    var description: String = ""
)

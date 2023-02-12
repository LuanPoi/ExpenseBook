package com.thepoi.expensebook.data.data_source.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.OffsetDateTime
import java.time.ZoneId

@Entity(tableName = "entry")
data class Entry(
    @PrimaryKey(autoGenerate = true)
    var uid: Long? = null,
    var date: OffsetDateTime = OffsetDateTime.now(ZoneId.systemDefault()),
    var value: Float = 0f,
    var description: String = ""
)

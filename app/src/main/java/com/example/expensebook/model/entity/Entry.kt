package com.example.expensebook.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.OffsetDateTime
import java.time.ZoneId

@Entity(tableName = "entry")
data class Entry(
    @PrimaryKey(autoGenerate = true)
    var uid: Int? = null,
    var date: OffsetDateTime = OffsetDateTime.now(ZoneId.systemDefault()),
    var value: Float = 0f,
    var description: String = ""
)

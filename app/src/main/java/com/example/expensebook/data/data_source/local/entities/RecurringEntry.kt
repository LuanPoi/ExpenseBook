package com.example.expensebook.data.data_source.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recurring_entry")
data class RecurringEntry(
    @PrimaryKey(autoGenerate = true)
    var uid: Long? = null,
    var value: Float = 0f,
    var description: String = ""
)
package com.example.expensebook.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recurring_entry")
data class RecurringEntry(
    @PrimaryKey(autoGenerate = true)
    var uid: Int? = null,
    var value: Float = 0f,
    var description: String = ""
)

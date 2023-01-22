package com.example.expensebook.ui.fragments.entry_details

import java.time.OffsetDateTime

data class EntryDetailsUiState (
    val uid: Long?,
    var date: OffsetDateTime,
    var isReceipt: Boolean,
    var value: Float,
    var description: String
)
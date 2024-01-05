package com.thepoi.expensebook.ui.fragments.entry_details

import java.time.ZonedDateTime

data class EntryDetailsUiState (
    val id: Long?,
    var datetime: ZonedDateTime,
    var isIncome: Boolean,
    var amount: Float?,
    var description: String
)
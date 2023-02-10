package com.example.expensebook.domain.model.filter

import java.time.OffsetDateTime

data class EntryFilter(
    var startOffsetDateTime: OffsetDateTime?,
    var exclusiveEndOffsetDateTime: OffsetDateTime?
)

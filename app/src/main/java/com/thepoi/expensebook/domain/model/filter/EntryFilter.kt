package com.thepoi.expensebook.domain.model.filter

import java.time.ZonedDateTime

data class EntryFilter(
    var startZonedDateTime: ZonedDateTime?,
    var exclusiveEndZonedDateTime: ZonedDateTime?
)

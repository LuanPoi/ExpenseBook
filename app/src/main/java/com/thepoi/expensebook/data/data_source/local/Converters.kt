package com.thepoi.expensebook.data.data_source.local

import androidx.room.TypeConverter
import java.time.Instant
import java.time.LocalDate
import java.time.OffsetDateTime
import java.time.YearMonth
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime

class Converters {
    @TypeConverter
    fun toYearMonth(epochDay: Long?): YearMonth? {
        return epochDay?.let { YearMonth.from(LocalDate.ofEpochDay(epochDay)) }
    }

    @TypeConverter
    fun fromYearMonth(yearMonth: YearMonth?): Long? {
        return yearMonth?.let{yearMonth.atDay(1).toEpochDay()}
    }

    @TypeConverter
    fun fromZonedDateTime(value: ZonedDateTime?): Long? {
        return value?.toEpochSecond()
    }

    @TypeConverter
    fun toZonedDateTime(epochSecond: Long?): ZonedDateTime? {
        return epochSecond?.let { ZonedDateTime.ofInstant(Instant.ofEpochSecond(epochSecond), ZoneId.systemDefault()) }
    }
}
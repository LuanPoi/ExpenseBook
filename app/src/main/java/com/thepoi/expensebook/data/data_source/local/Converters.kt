package com.thepoi.expensebook.data.data_source.local

import androidx.room.TypeConverter
import java.time.Instant
import java.time.LocalDate
import java.time.OffsetDateTime
import java.time.YearMonth
import java.time.ZoneOffset

class Converters {
    @TypeConverter
    fun toYearMonth(value: Long?): YearMonth? {
        return value?.let { YearMonth.from(LocalDate.ofEpochDay(it)) }
    }

    @TypeConverter
    fun fromYearMonth(value: YearMonth?): Long? {
        return value?.atDay(1)?.toEpochDay()
    }

    @TypeConverter
    fun fromOffsetDateTime(value: OffsetDateTime?): Long? {
        return value?.withOffsetSameInstant(ZoneOffset.UTC)?.toEpochSecond()
    }

    @TypeConverter
    fun toOffsetDateTime(value: Long?): OffsetDateTime? {
        return value?.let { OffsetDateTime.from(Instant.ofEpochSecond(it).atZone(ZoneOffset.systemDefault())) }
    }
}
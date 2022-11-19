package com.example.expensebook.data

import androidx.room.TypeConverter
import java.time.*

class Converters {
    @TypeConverter
    fun longToYearMonth(value: Long?): YearMonth? {
        return value?.let { YearMonth.from(LocalDate.ofEpochDay(it)) }
    }

    @TypeConverter
    fun yearMonthToLong(value: YearMonth?): Long? {
        return value?.atDay(1)?.toEpochDay()
    }

    @TypeConverter
    fun offsetDateTimeToLong(value: OffsetDateTime?): Long? {
        return value?.withOffsetSameInstant(ZoneOffset.UTC)?.toEpochSecond()
    }

    @TypeConverter
    fun longToOffsetDateTime(value: Long?): OffsetDateTime? {
        return value?.let { OffsetDateTime.from(Instant.ofEpochSecond(it).atZone(ZoneOffset.systemDefault())) }
    }
}
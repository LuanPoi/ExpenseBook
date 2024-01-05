package com.thepoi.expensebook.domain.usecase

import com.thepoi.expensebook.data.data_source.local.entities.Entry
import com.thepoi.expensebook.domain.model.filter.EntryFilter
import com.thepoi.expensebook.domain.repository.EntryRepository
import kotlinx.coroutines.flow.Flow
import java.time.OffsetDateTime
import java.time.YearMonth
import java.time.ZoneOffset
import java.time.ZonedDateTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FetchMonthEntriesUseCase @Inject constructor(
    private val entryRepository: EntryRepository
) {
    operator fun invoke(date: YearMonth): Flow<List<Entry>> {
        return entryRepository.getAllWithFilter(EntryFilter(
            ZonedDateTime.from(
                date.atDay(1).atStartOfDay().atZone(ZoneOffset.systemDefault())
            ),
            ZonedDateTime.from(
                date.plusMonths(1).atDay(1).atStartOfDay().atZone(ZoneOffset.systemDefault())
            )
        ))
    }
}
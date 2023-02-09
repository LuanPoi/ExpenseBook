package com.example.expensebook.domain.usecase

import com.example.expensebook.data.data_source.local.entities.Entry
import com.example.expensebook.domain.repository.EntryRepository
import kotlinx.coroutines.flow.Flow
import java.time.YearMonth
import javax.inject.Inject

class FetchMonthEntriesUseCase @Inject constructor(
    private val entryRepository: EntryRepository
) {
    suspend operator fun invoke(date: YearMonth): Flow<List<Entry>> {
        return entryRepository.getAll(date)
    }
}
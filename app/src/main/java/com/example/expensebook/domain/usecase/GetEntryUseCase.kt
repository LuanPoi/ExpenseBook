package com.example.expensebook.domain.usecase

import com.example.expensebook.data.data_source.local.entities.Entry
import com.example.expensebook.domain.repository.EntryRepository
import kotlinx.coroutines.flow.Flow

class GetEntryUseCase(
    private val entryRepository: EntryRepository
) {
    suspend operator fun invoke(id: Long): Flow<Entry> {
        return entryRepository.getById(id)
    }
}
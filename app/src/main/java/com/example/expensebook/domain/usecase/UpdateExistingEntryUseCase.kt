package com.example.expensebook.domain.usecase

import com.example.expensebook.data.data_source.local.entities.Entry
import com.example.expensebook.domain.repository.EntryRepository

class UpdateExistingEntryUseCase(
    private val entryRepository: EntryRepository
) {
    suspend operator fun invoke(entry: Entry){
        entryRepository.update(entry)
    }
}
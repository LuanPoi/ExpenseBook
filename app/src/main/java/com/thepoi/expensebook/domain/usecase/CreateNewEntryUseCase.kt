package com.thepoi.expensebook.domain.usecase

import com.thepoi.expensebook.data.data_source.local.entities.Entry
import com.thepoi.expensebook.domain.repository.EntryRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CreateNewEntryUseCase @Inject constructor(
    private val entryRepository: EntryRepository
) {

    suspend operator fun invoke(entry: Entry){
        entryRepository.insert(entry)
    }
}
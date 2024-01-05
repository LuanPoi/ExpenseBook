package com.thepoi.expensebook.domain.usecase

import com.thepoi.expensebook.data.data_source.local.entities.Entry
import com.thepoi.expensebook.domain.repository.EntryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetEntryUseCase @Inject constructor(
    private val entryRepository: EntryRepository
) {
    suspend operator fun invoke(id: Long): Flow<Entry?> {
        return entryRepository.getById(id)
    }
}
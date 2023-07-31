package com.thepoi.expensebook.domain.usecase

import com.thepoi.expensebook.data.data_source.local.entities.Entry
import com.thepoi.expensebook.domain.repository.EntryRepository
import com.thepoi.expensebook.domain.repository.MonthlyExpenseRepository
import java.time.YearMonth
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CreateNewEntryUseCase @Inject constructor(
    private val entryRepository: EntryRepository,
    private val monthlyExpenseRepository: MonthlyExpenseRepository
) {

    suspend operator fun invoke(entry: Entry){
        entryRepository.insert(entry)
        monthlyExpenseRepository.createEmptyIfDontExist(YearMonth.from(entry.date))
    }
}
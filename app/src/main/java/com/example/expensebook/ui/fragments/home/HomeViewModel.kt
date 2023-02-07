package com.example.expensebook.ui.fragments.home

import androidx.lifecycle.*
import com.example.expensebook.data.data_source.local.entities.Entry
import com.example.expensebook.data.data_source.local.entities.MonthlyExpense
import com.example.expensebook.data.repository.EntryRepositoryImpl
import com.example.expensebook.data.repository.MonthlyExpenseRepositoryImpl
import com.example.expensebook.domain.repository.RecurringEntryRepository
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import java.time.YearMonth
import java.time.ZoneId

class HomeViewModel(
    private val entryRepository: EntryRepositoryImpl,
    private val monthlyExpenseRepository: MonthlyExpenseRepositoryImpl,
    private val recurringEntryRepository: RecurringEntryRepository
): ViewModel() {

    private val _uiState: LiveData<HomeUiState> by lazy {
        var yearMonth = YearMonth.now(ZoneId.systemDefault())
        combine(
            monthlyExpenseRepository.getByDate(yearMonth),
            entryRepository.getAll(yearMonth),
            recurringEntryRepository.getAll()
        ) { _monthlyExpense, _entries, _recurringEntries ->
            var monthlyExpense = _monthlyExpense
            var entries = _entries
            var recurringEntries = _recurringEntries
            if(monthlyExpense == null) {
                monthlyExpense = MonthlyExpense(yearMonth)
                monthlyExpenseRepository.insert(monthlyExpense)
            }
            if(entries == null) {entries = listOf()}
            if(recurringEntries == null) {recurringEntries = listOf()}
            HomeUiState(yearMonth, monthlyExpense, entries, recurringEntries)
        }.distinctUntilChanged().asLiveData()
    }

    fun stateOnceAndStream(): LiveData<HomeUiState> = _uiState

    fun deleteEntry(entry: Entry){
        viewModelScope.launch{
            entryRepository.delete(entry)
        }
    }

    class Factory(
        private val entryRepository: EntryRepositoryImpl,
        private val monthlyExpenseRepository: MonthlyExpenseRepositoryImpl,
        private val recurringEntryRepository: RecurringEntryRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return HomeViewModel(entryRepository, monthlyExpenseRepository, recurringEntryRepository) as T
        }
    }
}
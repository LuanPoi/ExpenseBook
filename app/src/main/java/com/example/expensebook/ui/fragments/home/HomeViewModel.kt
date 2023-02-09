package com.example.expensebook.ui.fragments.home

import androidx.lifecycle.*
import com.example.expensebook.data.data_source.local.entities.Entry
import com.example.expensebook.data.data_source.local.entities.MonthlyExpense
import com.example.expensebook.domain.repository.EntryRepository
import com.example.expensebook.domain.repository.MonthlyExpenseRepository
import com.example.expensebook.domain.repository.RecurringEntryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import java.time.YearMonth
import java.time.ZoneId
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val entryRepository: EntryRepository,
    private val monthlyExpenseRepository: MonthlyExpenseRepository,
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
}
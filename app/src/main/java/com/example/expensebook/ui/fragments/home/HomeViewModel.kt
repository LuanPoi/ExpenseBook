package com.example.expensebook.ui.fragments.home

import android.content.Context
import androidx.lifecycle.*
import com.example.expensebook.data.model.entity.Entry
import com.example.expensebook.data.model.entity.MonthlyExpense
import com.example.expensebook.data.repository.EntryRepository
import com.example.expensebook.data.repository.MonthlyExpenseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.onEmpty
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.time.YearMonth
import java.time.ZoneId

class HomeViewModel(
    private val entryRepository: EntryRepository,
    private val monthlyExpenseRepository: MonthlyExpenseRepository
): ViewModel() {

    private val _uiState: LiveData<HomeUiState> by lazy {
        var yearMonth = YearMonth.now(ZoneId.systemDefault())
        combine(
            monthlyExpenseRepository.getMonthlyExpenseByDate(yearMonth),
            entryRepository.getEntries(yearMonth)
        ) { _monthlyExpense, _entries ->
            var monthlyExpense = _monthlyExpense
            var entries = _entries
            if(monthlyExpense == null) {
                monthlyExpense = MonthlyExpense(yearMonth)
                monthlyExpenseRepository.addMonthlyExpense(monthlyExpense)
            }
            if(entries == null) {entries = listOf()}
            HomeUiState(yearMonth, monthlyExpense, entries)
        }.distinctUntilChanged().asLiveData()
    }

    fun stateOnceAndStream(): LiveData<HomeUiState> = _uiState

    fun deleteEntry(entry: Entry){
        viewModelScope.launch{
            entryRepository.deleteEntry(entry)
        }
    }

    class Factory(
        private val entryRepository: EntryRepository,
        private val monthlyExpenseRepository: MonthlyExpenseRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return HomeViewModel(entryRepository, monthlyExpenseRepository) as T
        }
    }
}
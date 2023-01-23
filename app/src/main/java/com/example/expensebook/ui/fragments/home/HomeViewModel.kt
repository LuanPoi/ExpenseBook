package com.example.expensebook.ui.fragments.home

import androidx.lifecycle.*
import com.example.expensebook.domain.model.Entry
import com.example.expensebook.domain.model.MonthlyExpense
import com.example.expensebook.data.repository.EntryRepositoryImpl
import com.example.expensebook.data.repository.MonthlyExpenseRepositoryImpl
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import java.time.YearMonth
import java.time.ZoneId

class HomeViewModel(
    private val entryRepository: EntryRepositoryImpl,
    private val monthlyExpenseRepository: MonthlyExpenseRepositoryImpl
): ViewModel() {

    private val _uiState: LiveData<HomeUiState> by lazy {
        var yearMonth = YearMonth.now(ZoneId.systemDefault())
        combine(
            monthlyExpenseRepository.getByDate(yearMonth),
            entryRepository.getAll(yearMonth)
        ) { _monthlyExpense, _entries ->
            var monthlyExpense = _monthlyExpense
            var entries = _entries
            if(monthlyExpense == null) {
                monthlyExpense = MonthlyExpense(yearMonth)
                monthlyExpenseRepository.insert(monthlyExpense)
            }
            if(entries == null) {entries = listOf()}
            HomeUiState(yearMonth, monthlyExpense, entries)
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
        private val monthlyExpenseRepository: MonthlyExpenseRepositoryImpl
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return HomeViewModel(entryRepository, monthlyExpenseRepository) as T
        }
    }
}
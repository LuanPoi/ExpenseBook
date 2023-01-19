package com.example.expensebook.view.fragments.home

import android.content.Context
import androidx.lifecycle.*
import com.example.expensebook.model.entity.Entry
import com.example.expensebook.model.entity.MonthlyExpense
import com.example.expensebook.repository.EntryRepository
import com.example.expensebook.repository.MonthlyExpenseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.time.YearMonth
import java.time.ZoneId

class HomeViewModel(
    private val entryRepository: EntryRepository,
    private val monthlyExpenseRepository: MonthlyExpenseRepository
): ViewModel() {

    private val _uiState: MutableLiveData<HomeUiState> by lazy {
        MutableLiveData<HomeUiState>(HomeUiState(MonthlyExpense(), emptyList())).apply {
            val yearMonthNow = YearMonth.now(ZoneId.systemDefault())
            viewModelScope.launch {
                val yearMonthNow = YearMonth.now(ZoneId.systemDefault())
                monthlyExpenseRepository.getMonthlyExpenseByDate(yearMonthNow).value?.let{ monthlyExpense ->
                    entryRepository.getEntries(yearMonthNow).value?.let {entries ->
                        value = HomeUiState(monthlyExpense, entries)
                    }
                } ?: monthlyExpenseRepository.addMonthlyExpense(MonthlyExpense(yearMonthNow, 0f, 0f, 0f, 0f))
            }
        }
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
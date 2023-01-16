package com.example.expensebook.view.fragments.home

import androidx.lifecycle.*
import com.example.expensebook.model.entity.Entry
import com.example.expensebook.repository.EntryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.YearMonth

class HomeViewModel(private val entryRepository: EntryRepository): ViewModel() {

    val entries: LiveData<List<Entry>> by lazy {
        entryRepository.getEntries(YearMonth.now())
    }

    fun deleteEntry(entry: Entry){
        viewModelScope.launch{
            entryRepository.deleteEntry(entry)
        }
    }
}
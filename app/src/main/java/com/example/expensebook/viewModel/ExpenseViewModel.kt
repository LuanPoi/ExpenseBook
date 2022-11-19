package com.example.expensebook.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensebook.data.LocalDatabase
import com.example.expensebook.model.entity.Entry
import com.example.expensebook.repository.EntryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ExpenseViewModel(application: Application): AndroidViewModel(application) {
    var readAllData: List<Entry> = listOf()
    private val repository: EntryRepository

    init {
        val entryDao = LocalDatabase.getDatabase(application).entryDao()
        repository = EntryRepository(entryDao)
        readAllData = repository.getAllEntries()
    }

    fun addEntry(entry: Entry){
        viewModelScope.launch(Dispatchers.IO) {
            repository.addEntry(entry)
        }
    }
}
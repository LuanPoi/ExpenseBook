package com.example.expensebook.view.fragments.entry_details

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensebook.model.entity.Entry
import com.example.expensebook.repository.EntryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EntryDetailsViewModel(application: Application) : ViewModel() {

    private val entryRepository: EntryRepository = EntryRepository(application)

    val entry: MutableLiveData<Entry> = MutableLiveData(Entry())

    fun save(entry: Entry){
        viewModelScope.launch(Dispatchers.IO) {
            entryRepository.addEntry(entry)
        }
    }
}
package com.example.expensebook.view.fragments.entry_details

import android.app.Application
import androidx.lifecycle.*
import com.example.expensebook.model.entity.Entry
import com.example.expensebook.repository.EntryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.OffsetDateTime

class EntryDetailsViewModel(private val entryRepository: EntryRepository) : ViewModel() {

    private val _uiState: MutableLiveData<EntryDetailsUiState> by lazy {
        MutableLiveData<EntryDetailsUiState>(EntryDetailsUiState(null, OffsetDateTime.now(), false, 0f, ""))
    }

    fun stateOnceAndStream(): LiveData<EntryDetailsUiState> = _uiState

    fun toggleEntryType(value: Boolean){
        _uiState.value = _uiState.value?.copy(isReceipt = value)
    }

    fun save(entry: Entry){
        viewModelScope.launch {
            entryRepository.addEntry(entry)
        }
    }

    class Factory(private val entryRepository: EntryRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return EntryDetailsViewModel(entryRepository) as T
        }
    }
}
package com.example.expensebook.ui.fragments.entry_details

import androidx.lifecycle.*
import com.example.expensebook.domain.model.Entry
import com.example.expensebook.domain.repository.EntryRepository
import kotlinx.coroutines.launch
import java.time.OffsetDateTime

class EntryDetailsViewModel(private val entryRepository: EntryRepository) : ViewModel() {

    private val _uiState: MutableLiveData<EntryDetailsUiState> by lazy {
        MutableLiveData<EntryDetailsUiState>(EntryDetailsUiState(null, OffsetDateTime.now(), false, 0f, ""))
    }

    fun stateOnceAndStream(): LiveData<EntryDetailsUiState> = _uiState

    fun updateValues(uiState: EntryDetailsUiState){
        _uiState.value = uiState
    }

    fun save(entry: Entry){
        viewModelScope.launch {
            entryRepository.insert(entry)
        }
    }

    class Factory(private val entryRepository: EntryRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return EntryDetailsViewModel(entryRepository) as T
        }
    }
}
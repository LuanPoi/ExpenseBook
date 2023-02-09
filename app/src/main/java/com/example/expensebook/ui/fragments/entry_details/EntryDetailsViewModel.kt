package com.example.expensebook.ui.fragments.entry_details

import androidx.lifecycle.*
import com.example.expensebook.data.data_source.local.entities.Entry
import com.example.expensebook.domain.repository.EntryRepository
import com.example.expensebook.domain.usecase.CreateNewEntryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.OffsetDateTime
import javax.inject.Inject


@HiltViewModel
class EntryDetailsViewModel @Inject constructor(
    private val createNewEntryUseCase: CreateNewEntryUseCase
) : ViewModel() {

    private val _uiState: MutableLiveData<EntryDetailsUiState> by lazy {
        MutableLiveData<EntryDetailsUiState>(EntryDetailsUiState(null, OffsetDateTime.now(), false, 0f, ""))
    }

    fun stateOnceAndStream(): LiveData<EntryDetailsUiState> = _uiState

    fun updateValues(uiState: EntryDetailsUiState){
        _uiState.value = uiState
    }

    fun save(entry: Entry){
        viewModelScope.launch {
            createNewEntryUseCase.invoke(entry)
        }
    }
}
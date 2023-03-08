package com.thepoi.expensebook.ui.fragments.entry_details

import androidx.lifecycle.*
import com.thepoi.expensebook.data.data_source.local.entities.Entry
import com.thepoi.expensebook.domain.usecase.CreateNewEntryUseCase
import com.thepoi.expensebook.domain.usecase.GetEntryUseCase
import com.thepoi.expensebook.domain.usecase.UpdateExistingEntryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.time.OffsetDateTime
import javax.inject.Inject


@HiltViewModel
class EntryDetailsViewModel @Inject constructor(
    private val createNewEntryUseCase: CreateNewEntryUseCase,
    private val updateExistingEntryUseCase: UpdateExistingEntryUseCase,
    private val getEntryUseCase: GetEntryUseCase
) : ViewModel() {

    private val _uiState: MutableLiveData<EntryDetailsUiState> by lazy {
        MutableLiveData<EntryDetailsUiState>(EntryDetailsUiState(null, OffsetDateTime.now(), false, null, ""))
    }

    fun stateOnceAndStream(entryId: Long? = null): LiveData<EntryDetailsUiState> {
        entryId?.let {
            viewModelScope.launch {
                getEntryUseCase(entryId).collect { entry ->
                    _uiState.value = EntryDetailsUiState(
                        entry.uid,
                        entry.date,
                        entry.value >= 0,
                        entry.value,
                        entry.description
                    )
                }
            }
        }
        return _uiState
    }

    fun updateValues(uiState: EntryDetailsUiState){
        _uiState.value = uiState
    }

    fun save(entry: Entry){
        viewModelScope.launch {
            entry.uid?.let {
                updateExistingEntryUseCase(entry)
            } ?: run {
                createNewEntryUseCase(entry)
            }
        }
    }
}
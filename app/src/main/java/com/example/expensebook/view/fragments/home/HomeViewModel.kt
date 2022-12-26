package com.example.expensebook.view.fragments.home

import android.app.Application
import androidx.lifecycle.*
import com.example.expensebook.data.LocalDatabase
import com.example.expensebook.model.entity.Entry
import com.example.expensebook.repository.EntryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.Month
import java.time.OffsetDateTime
import java.time.Year
import java.time.YearMonth

class HomeViewModel(application: Application): ViewModel() {

    private val entryRepository: EntryRepository = EntryRepository(application)

    val entries: LiveData<List<Entry>> by lazy {
        entryRepository.getEntries(YearMonth.now())
    }
}
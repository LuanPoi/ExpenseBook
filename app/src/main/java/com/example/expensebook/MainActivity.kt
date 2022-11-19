package com.example.expensebook

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.expensebook.databinding.ActivityMainBinding
import com.example.expensebook.model.entity.Entry
import com.example.expensebook.repository.EntryRepository
import com.example.expensebook.view.ExpenseHistoryListAdapter
import com.example.expensebook.viewModel.ExpenseViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.time.OffsetDateTime

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private lateinit var adapter: ExpenseHistoryListAdapter

    private lateinit var expenseViewModel: ExpenseViewModel

    private var mockData: List<Entry> = listOf(Entry(1, OffsetDateTime.now(), -246.0f, "Compra de hoje"))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        GlobalScope.launch {
            adapter = ExpenseHistoryListAdapter()
            binding.recyclerViewExpenseHistoryList.layoutManager = LinearLayoutManager(this@MainActivity)
            binding.recyclerViewExpenseHistoryList.adapter = adapter

            expenseViewModel = ExpenseViewModel(application)
//            Campo para adicionar uma entrada qualquer ao banco, só para popula-lo
//            expenseViewModel.addEntry(Entry(null, OffsetDateTime.now(), 50.0f, "Grana do freelance"))
            adapter.setData(expenseViewModel.readAllData)
        }
    }
}
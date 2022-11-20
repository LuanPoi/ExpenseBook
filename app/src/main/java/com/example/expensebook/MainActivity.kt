package com.example.expensebook

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.expensebook.databinding.ActivityMainBinding
import com.example.expensebook.model.EnumItemViewType
import com.example.expensebook.view.MainRecyclerViewListAdapter
import com.example.expensebook.viewModel.ExpenseViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {



    private lateinit var binding: ActivityMainBinding

    private lateinit var adapter: MainRecyclerViewListAdapter

    private lateinit var expenseViewModel: ExpenseViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        GlobalScope.launch {
            adapter = MainRecyclerViewListAdapter()
            binding.recyclerViewMain.layoutManager = LinearLayoutManager(this@MainActivity)
            binding.recyclerViewMain.adapter = adapter
            expenseViewModel = ExpenseViewModel(application)

            val auxList: ArrayList<Pair<EnumItemViewType, Any>> = arrayListOf()
            auxList.add(Pair(EnumItemViewType.TITLE, resources.getString(R.string.expense_history_title)))
            auxList.addAll(expenseViewModel.readAllData.map { x -> Pair(EnumItemViewType.EXPENSE_ITEM, x) })

            adapter.setData(auxList)
        }
    }
}
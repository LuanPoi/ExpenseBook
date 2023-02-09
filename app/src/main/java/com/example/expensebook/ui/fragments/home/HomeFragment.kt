package com.example.expensebook.ui.fragments.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.expensebook.R
import com.example.expensebook.data.data_source.local.LocalDatabase
import com.example.expensebook.data.repository.EntryRepositoryImpl
import com.example.expensebook.data.repository.MonthlyExpenseRepositoryImpl
import com.example.expensebook.data.repository.RecurringEntryRepositoryImpl
import com.example.expensebook.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var homeViewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = HomeRecyclerViewListAdapter(homeViewModel)
        binding.recyclerViewHome.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewHome.adapter = adapter


        homeViewModel.stateOnceAndStream().observe(viewLifecycleOwner) { uiState ->
            val auxList: ArrayList<Pair<EnumItemViewType, Any>> = arrayListOf()
            auxList.add(Pair(EnumItemViewType.MONTH_EXPENSE_CONTAINER, uiState))
            auxList.add(Pair(EnumItemViewType.DAY_EXPENSE_CONTAINER, uiState))
            auxList.add(Pair(EnumItemViewType.TITLE, resources.getString(R.string.expense_history_title)))
            auxList.addAll(uiState.currentEntries.map { entry -> Pair(EnumItemViewType.EXPENSE_ITEM, entry) })
            adapter.setData(auxList)
        }

        binding.floatingActionButton.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_entryDetailsFragment)
        }
    }
}
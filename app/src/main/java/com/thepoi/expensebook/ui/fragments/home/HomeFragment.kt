package com.thepoi.expensebook.ui.fragments.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.thepoi.expensebook.R
import com.thepoi.expensebook.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var homeViewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        runBlocking {
            if(!homeViewModel.currentMonthExpenseExist()) findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToOnboardFragment(false))
        }
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
            val recyclerViewItems: ArrayList<Pair<EnumItemViewType, Any>> = arrayListOf()

            recyclerViewItems.add(Pair(EnumItemViewType.MONTH_EXPENSE_CONTAINER, uiState.monthDataUiState))
            uiState.dayDataUiState?.let { recyclerViewItems.add(Pair(EnumItemViewType.DAY_EXPENSE_CONTAINER, it)) }
            recyclerViewItems.add(Pair(EnumItemViewType.TITLE, resources.getString(R.string.expense_history_title)))
            recyclerViewItems.addAll(uiState.entriesHistoryUiState.map { entry -> Pair(EnumItemViewType.EXPENSE_ITEM, entry) })

            adapter.setData(recyclerViewItems)
        }

        binding.floatingActionButton.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_entryDetailsFragment)
        }
    }
}
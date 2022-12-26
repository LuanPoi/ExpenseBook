package com.example.expensebook.view.fragments.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.expensebook.R
import com.example.expensebook.databinding.FragmentHomeBinding
import com.example.expensebook.model.EnumItemViewType
import com.example.expensebook.model.entity.Entry
import java.time.OffsetDateTime

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var homeViewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        homeViewModel = HomeViewModel(requireActivity().application)
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

        val adapter = HomeRecyclerViewListAdapter()
        binding.recyclerViewHome.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewHome.adapter = adapter


        homeViewModel.entries.observe(viewLifecycleOwner) { entries ->
            val auxList: ArrayList<Pair<EnumItemViewType, Any>> = arrayListOf()
            auxList.add(Pair(EnumItemViewType.TITLE, resources.getString(R.string.expense_history_title)))
            auxList.addAll(entries.map { entry -> Pair(EnumItemViewType.EXPENSE_ITEM, entry) })
            adapter.setData(auxList)
        }

        binding.floatingActionButton.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_entryDetailsFragment)
        }
    }
}
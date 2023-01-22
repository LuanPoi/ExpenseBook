package com.example.expensebook.ui.fragments.onboard

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.expensebook.R
import com.example.expensebook.data.model.entity.MonthlyExpense
import com.example.expensebook.data.repository.MonthlyExpenseRepository
import com.example.expensebook.databinding.FragmentOnboardBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.YearMonth

class OnboardFragment : Fragment() {

    private lateinit var binding: FragmentOnboardBinding

    private val repository by lazy {
        MonthlyExpenseRepository(requireActivity().application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentOnboardBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            repository.getMonthlyExpenseByDate(YearMonth.now()).asLiveData().observe(viewLifecycleOwner) {
                if(it != null){
                    binding.editTextInitialValue.setText(it.initial_value.toString())
                    binding.editTextSavingsGoal.setText(it.savings_goal.toString())
                }
            }
        }

        binding.buttonSave.setOnClickListener {
            if(binding.editTextInitialValue.text.isNotBlank() && binding.editTextSavingsGoal.text.isNotBlank()){
                lifecycleScope.launch {
                    repository.addMonthlyExpense(
                        MonthlyExpense(
                            initial_value = binding.editTextInitialValue.text.toString().toFloat(),
                            savings_goal = binding.editTextSavingsGoal.text.toString().toFloat()
                        )
                    )
                }
                findNavController().navigate(R.id.action_onboardFragment_to_homeFragment)
            }
        }

        binding.buttonSkip.setOnClickListener {
            findNavController().navigate(R.id.action_onboardFragment_to_homeFragment)
        }
    }
}
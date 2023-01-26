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
import com.example.expensebook.domain.model.MonthlyExpense
import com.example.expensebook.data.repository.MonthlyExpenseRepositoryImpl
import com.example.expensebook.databinding.FragmentOnboardBinding
import kotlinx.coroutines.launch
import java.time.YearMonth

class OnboardFragment : Fragment() {

    private lateinit var binding: FragmentOnboardBinding

    private var monthlyExpense: MonthlyExpense? = null

    private val repository by lazy {
        MonthlyExpenseRepositoryImpl(requireActivity().application)
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
            repository.getByDate(YearMonth.now()).asLiveData().observe(viewLifecycleOwner) {
                if(it != null){
                    monthlyExpense = it
                    binding.editTextInitialValue.setText(it.initial_value.toString())
                    binding.editTextSavingsGoal.setText(it.savings_goal.toString())
                }
            }
        }

        binding.buttonSave.setOnClickListener {
            if(binding.editTextInitialValue.text.isNotBlank() && binding.editTextSavingsGoal.text.isNotBlank()){
                if(monthlyExpense == null){
                    monthlyExpense = MonthlyExpense()
                    monthlyExpense?.apply {
                        initial_value = binding.editTextInitialValue.text.toString().toFloat()
                        savings_goal = binding.editTextSavingsGoal.text.toString().toFloat()
                    }
                    lifecycleScope.launch {
                        monthlyExpense?.let{
                            repository.insert(it)
                        }
                    }
                } else {
                    monthlyExpense?.apply {
                        initial_value = binding.editTextInitialValue.text.toString().toFloat()
                        savings_goal = binding.editTextSavingsGoal.text.toString().toFloat()
                    }
                    lifecycleScope.launch {
                        monthlyExpense?.let{
                            repository.update(it)
                        }
                    }
                }
                findNavController().navigate(R.id.action_onboardFragment_to_homeFragment)
            }
        }

        binding.buttonSkip.setOnClickListener {
            findNavController().navigate(R.id.action_onboardFragment_to_homeFragment)
        }
    }
}
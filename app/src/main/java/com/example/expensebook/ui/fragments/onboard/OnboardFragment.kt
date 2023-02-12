package com.example.expensebook.ui.fragments.onboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.expensebook.R
import com.example.expensebook.data.data_source.local.LocalDatabase
import com.example.expensebook.data.data_source.local.entities.MonthlyExpense
import com.example.expensebook.data.repository.MonthlyExpenseRepositoryImpl
import com.example.expensebook.databinding.FragmentOnboardBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.time.YearMonth

@AndroidEntryPoint
class OnboardFragment : Fragment() {

    private val args: OnboardFragmentArgs by navArgs()
    private lateinit var binding: FragmentOnboardBinding

    private var monthlyExpense: MonthlyExpense? = null

    private val repository by lazy {
        MonthlyExpenseRepositoryImpl(LocalDatabase.getDatabase(requireContext()))
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
                findNavController().navigateUp()
            }
        }

        binding.buttonSkip.setOnClickListener {
            if(args.monthlyExpenseAlreadyExist) findNavController().navigateUp()
        }
    }
}
package com.thepoi.expensebook.ui.fragments.initialize_month

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.thepoi.expensebook.R
import com.thepoi.expensebook.data.data_source.local.entities.MonthlyExpense
import com.thepoi.expensebook.databinding.FragmentInitializeMonthBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.time.YearMonth

@AndroidEntryPoint
class InitializeMonthFragment : Fragment() {

    private val args: InitializeMonthFragmentArgs by navArgs()
    private lateinit var binding: FragmentInitializeMonthBinding
    private lateinit var viewModel: InitializeMonthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this)[InitializeMonthViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentInitializeMonthBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val lastMonthlyExpenseDate: YearMonth? = args.lastMonthlyExpenseDate

        lastMonthlyExpenseDate?.let { lastDate ->
            binding.buttonLoadPreviousData.visibility = View.VISIBLE
            binding.buttonLoadPreviousData.setOnClickListener {
                viewModel.getMonthlyExpense(lastDate).observe(viewLifecycleOwner) {response ->
                    response?.let {
                        binding.textInputInitialValue.editText?.setText(it.initial_value.toInt().toString())
                        binding.textInputEconomyGoal.editText?.setText(it.savings_goal.toInt().toString())
                    }
                }
            }
        }

        binding.buttonConfirm.setOnClickListener {
            val initialValue: String = binding.textInputInitialValue.editText?.text.toString()
            val economyGoal: String = binding.textInputEconomyGoal.editText?.text.toString()

            if(initialValue.isBlank() || initialValue.isEmpty()) return@setOnClickListener
            if(economyGoal.isBlank() || economyGoal.isEmpty()) return@setOnClickListener

            lifecycleScope.launch {
                viewModel.createNewMonthlyExpense(MonthlyExpense(
                    initial_value = initialValue.toFloat(),
                    savings_goal = economyGoal.toFloat()
                )).also {
                    findNavController().navigate(R.id.action_initializeMonthFragment_to_homeFragment)
                }
            }
        }
    }
}
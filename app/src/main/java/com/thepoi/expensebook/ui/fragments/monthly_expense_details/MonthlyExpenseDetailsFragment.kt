package com.thepoi.expensebook.ui.fragments.monthly_expense_details

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import com.thepoi.expensebook.databinding.FragmentMonthlyExpenseDetailsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MonthlyExpenseDetailsFragment(
    private val initialValue: Float,
    private val savingsGoal: Float,
    private val onConfirm: (newInitialValue: Float, newSavingsGoal: Float) -> Unit
) : DialogFragment() {

    private lateinit var binding: FragmentMonthlyExpenseDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        dialog?.let {dialog ->
            dialog.window?.let {window ->
                window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
                window.requestFeature(Window.FEATURE_NO_TITLE);
                window.setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            }
        }
        binding = FragmentMonthlyExpenseDetailsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.textInputInitialValue.editText?.setText("%.2f".format(initialValue))
        binding.textInputEconomyGoal.editText?.setText("%.2f".format(savingsGoal))

        binding.buttonCancel.setOnClickListener {
            dismiss()
        }
        
        binding.buttonConfirm.setOnClickListener {
            var newInitialValue: Float = binding.textInputInitialValue.editText?.text.toString().trim().replace(".", "").replace(",", ".").toFloat()
            var newSavingsGoal: Float = binding.textInputEconomyGoal.editText?.text.toString().trim().replace(".", "").replace(",", ".").toFloat()
            onConfirm(newInitialValue, newSavingsGoal)
            dismiss()
        }
    }
}
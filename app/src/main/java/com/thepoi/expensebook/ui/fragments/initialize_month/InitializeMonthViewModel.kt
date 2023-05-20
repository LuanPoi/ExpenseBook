package com.thepoi.expensebook.ui.fragments.initialize_month

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.thepoi.expensebook.data.data_source.local.entities.MonthlyExpense
import com.thepoi.expensebook.domain.usecase.CreateNewMonthlyExpenseUseCase
import com.thepoi.expensebook.domain.usecase.GetMonthlyExpenseUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.YearMonth
import javax.inject.Inject

@HiltViewModel
class InitializeMonthViewModel @Inject constructor(
    private val getMonthlyExpenseUseCase: GetMonthlyExpenseUseCase,
    private val createNewMonthlyExpenseUseCase: CreateNewMonthlyExpenseUseCase
) : ViewModel() {
    fun getMonthlyExpense(date: YearMonth): LiveData<MonthlyExpense?> {
        return getMonthlyExpenseUseCase(date).asLiveData()
    }

    suspend fun createNewMonthlyExpense(monthlyExpense: MonthlyExpense): Long {
        return createNewMonthlyExpenseUseCase(monthlyExpense)
    }
}
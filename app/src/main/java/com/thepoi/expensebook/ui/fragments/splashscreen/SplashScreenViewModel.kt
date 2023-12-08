package com.thepoi.expensebook.ui.fragments.splashscreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.thepoi.expensebook.data.data_source.local.entities.MonthlyExpense
import com.thepoi.expensebook.domain.usecase.GetMostRecentMonthlyExpenseUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashScreenViewModel @Inject constructor (
    private val getMostRecentMonthlyExpenseUseCase: GetMostRecentMonthlyExpenseUseCase
): ViewModel() {
    fun checkMonthlyExpenseExistence(): LiveData<MonthlyExpense?> {
        return getMostRecentMonthlyExpenseUseCase().asLiveData()
    }
}
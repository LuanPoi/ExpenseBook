package com.thepoi.expensebook.domain.usecase

import com.thepoi.expensebook.data.data_source.local.entities.MonthlyExpense
import com.thepoi.expensebook.domain.repository.MonthlyExpenseRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CreateNewMonthlyExpenseUseCase @Inject constructor(
    private val monthlyExpenseRepository: MonthlyExpenseRepository
) {
    suspend operator fun invoke(monthlyExpense: MonthlyExpense){
        monthlyExpenseRepository.insert(monthlyExpense)
    }
}
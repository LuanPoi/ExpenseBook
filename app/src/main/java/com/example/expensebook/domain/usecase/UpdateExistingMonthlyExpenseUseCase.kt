package com.example.expensebook.domain.usecase

import com.example.expensebook.data.data_source.local.entities.MonthlyExpense
import com.example.expensebook.domain.repository.MonthlyExpenseRepository

class UpdateExistingMonthlyExpenseUseCase(
    private val monthlyExpenseRepository: MonthlyExpenseRepository
) {
    suspend operator fun invoke(monthlyExpense: MonthlyExpense){
        monthlyExpenseRepository.update(monthlyExpense)
    }
}
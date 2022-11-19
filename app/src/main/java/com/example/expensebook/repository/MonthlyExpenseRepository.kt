package com.example.expensebook.repository

import com.example.expensebook.data.dao.MonthlyExpenseDao
import com.example.expensebook.model.entity.MonthlyExpense

class MonthlyExpenseRepository(private val monthlyExpenseDao: MonthlyExpenseDao) {

    fun addMonthlyExpense(monthlyExpense: MonthlyExpense){
        monthlyExpenseDao.addMonthlyExpense(monthlyExpense)
    }

    fun getAllMonthlyExpenses(): List<MonthlyExpense>{
        return monthlyExpenseDao.getAllMonthlyExpenses()
    }

    fun getMonthlyExpenseById(monthlyExpenseId: Int): MonthlyExpense{
        return monthlyExpenseDao.getMonthlyExpenseById(monthlyExpenseId)
    }

    fun updateMonthlyExpense(monthlyExpense: MonthlyExpense){
        monthlyExpenseDao.updateMonthlyExpense(monthlyExpense)
    }

    fun deleteMonthlyExpense(monthlyExpense: MonthlyExpense){
        monthlyExpenseDao.deleteMonthlyExpense(monthlyExpense)
    }
}
package com.example.expensebook.domain.model.filter

data class RecurringEntryFilter (
    var getExpenses: Boolean? = true,
    var getIncomes: Boolean? = true
)
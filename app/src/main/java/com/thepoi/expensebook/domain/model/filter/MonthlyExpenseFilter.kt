package com.thepoi.expensebook.domain.model.filter

import java.time.YearMonth

data class MonthlyExpenseFilter(
    var startYearMonth: YearMonth?,
    var endYearMonth: YearMonth?
)

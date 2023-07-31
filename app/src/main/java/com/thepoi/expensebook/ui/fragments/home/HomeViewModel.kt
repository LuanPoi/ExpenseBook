package com.thepoi.expensebook.ui.fragments.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thepoi.expensebook.data.data_source.local.entities.Entry
import com.thepoi.expensebook.domain.usecase.DeleteEntryUseCase
import com.thepoi.expensebook.domain.usecase.FetchMonthDataUseCase
import com.thepoi.expensebook.domain.usecase.GetAllMonthlyExpenseDatesUseCase
import com.thepoi.expensebook.domain.usecase.GetMonthlyExpenseUseCase
import com.thepoi.expensebook.domain.usecase.GetNextAndPreviousMonthIdsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Thread.sleep
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import kotlin.math.absoluteValue
import kotlin.math.roundToInt

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val fetchMonthDataUseCase: FetchMonthDataUseCase,
    private val deleteEntryUseCase: DeleteEntryUseCase,
    private val getMonthlyExpenseUseCase: GetMonthlyExpenseUseCase,
    private val getAllMonthlyExpenseDatesUseCase: GetAllMonthlyExpenseDatesUseCase,
    private val getNextAndPreviousMonthIdsUseCase: GetNextAndPreviousMonthIdsUseCase
): ViewModel() {

    private val _monthlyExpenseDates: StateFlow<List<YearMonth>> = loadMonthlyExpenseDates()
    private val _selectedMonth: MutableStateFlow<YearMonth> = MutableStateFlow(YearMonth.now())
    var uiState: MutableLiveData<HomeUiState> = MutableLiveData()

    init {
        viewModelScope.launch {
            _selectedMonth.collect { selectedMonth ->
                fetchMonthDataUseCase(selectedMonth).collect { monthData ->
                    getNextAndPreviousMonthIdsUseCase(selectedMonth).collect {
                        uiState.value = HomeUiState(
                            monthDataUiState = HomeUiState.MonthDataUiState(
                                monthNameOrdinal = monthData.date.month.ordinal,
                                expend = with(monthData.totalExpend.times(-1)) {
                                    "R$ %.2f".format(if (this <= 0) 0f else this)
                                },
                                remaining = "R$ %.2f".format(monthData.remainingAmount),
                                percentageExpend = with(monthData.totalExpend.times(-1)) {
                                    if (monthData.initialValue <= 0 || this <= 0) 0 else (this / monthData.initialValue * 100).roundToInt()
                                },
                                initialValue = "R$ %.2f".format(monthData.initialValue),
                                savingsGoal = "R$ %.2f".format(monthData.savingsGoal),
                                idOfPreviousMonthWithData = it.first,
                                idOfNextMonthWithData = it.second
                            ),
                            dayDataUiState = monthData.currentDayData?.run {
                                HomeUiState.DayDataUiState(
                                    recommendedDailyExpense = "R$ %.2f".format(recommendedExpendValue),
                                    expendToday = with(expendToday.times(-1)) {
                                        "R$ %.2f".format(if (this <= 0) 0f else this)
                                    },
                                    remainingToday = "R$ %.2f".format(remainingToday)
                                )
                            },
                            entriesHistoryUiState = monthData.entries.map { entry ->
                                HomeUiState.EntryUiState(
                                    id = entry.uid!!,
                                    icon = "@drawable/ic_money",
                                    description = entry.description,
                                    value = if (entry.value >= 0) "+ R$ %.2f".format(entry.value) else "- R$ %.2f".format(entry.value.absoluteValue),
                                    date = entry.date.format(DateTimeFormatter.ofPattern("dd/MM"))
                                )
                            }
                        )
                    }
                }
            }
        }
        viewModelScope.launch {
            // Testando atualização de UI ao trocar de mês
            withContext(Dispatchers.Default){
                sleep(5000)
                setSelectedMonth(YearMonth.now().minusMonths(1))
                sleep(5000)
                setSelectedMonth(YearMonth.now())
            }
        }
    }

    fun loadMonthlyExpenseDates(): StateFlow<List<YearMonth>> {
        return getAllMonthlyExpenseDatesUseCase.invoke().stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            emptyList()
        )
    }

    fun setSelectedMonth(date: YearMonth){
        viewModelScope.launch {
            _selectedMonth.emit(date)
        }
    }

    suspend fun currentMonthExpenseExist(): Boolean {
        return (getMonthlyExpenseUseCase(YearMonth.now()).firstOrNull() != null)
    }

    fun deleteEntry(entry: Entry){
        viewModelScope.launch{
            deleteEntryUseCase.invoke(entry)
        }
    }
}
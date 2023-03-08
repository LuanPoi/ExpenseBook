package com.thepoi.expensebook.ui.fragments.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.thepoi.expensebook.data.data_source.local.entities.Entry
import com.thepoi.expensebook.domain.usecase.DeleteEntryUseCase
import com.thepoi.expensebook.domain.usecase.FetchMonthDataUseCase
import com.thepoi.expensebook.domain.usecase.GetMonthlyExpenseUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.time.YearMonth
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import kotlin.math.absoluteValue
import kotlin.math.roundToInt

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val fetchMonthDataUseCase: FetchMonthDataUseCase,
    private val deleteEntryUseCase: DeleteEntryUseCase,
    private val getMonthlyExpenseUseCase: GetMonthlyExpenseUseCase
): ViewModel() {

    private val _uiState: LiveData<HomeUiState> by lazy {
        var yearMonth = YearMonth.now(ZoneId.systemDefault())
        fetchMonthDataUseCase(yearMonth).map {
            HomeUiState(
                HomeUiState.MonthDataUiState(
                    it.date.month.ordinal,
                    with(it.totalExpend.times(-1)){
                        "R$ %.2f".format(if(this <= 0) 0f else this)
                    },
                    "R$ %.2f".format(it.remainingAmount),
                    //add the percertage of the expend amount, treating the case when the initial value is 0 or negative and rounding the value to the nearest integer
                    with(it.totalExpend.times(-1)){
                        if(it.initialValue <= 0 || this <= 0) 0 else (this / it.initialValue * 100).roundToInt()
                    },
                    "R$ %.2f".format(it.initialValue),
                    "R$ %.2f".format(it.savingsGoal)
                ),
                it.currentDayData?.run {
                    HomeUiState.DayDataUiState(
                        "R$ %.2f".format(recommendedExpendValue),
                        with(expendToday.times(-1)){
                            "R$ %.2f".format(if(this <= 0) 0f else this)
                        },
                        "R$ %.2f".format(remainingToday)
                    )
                },
                it.entries.map { entry ->
                    HomeUiState.EntryUiState(
                        id = entry.uid!!,
                        description = entry.description,
                        value = if(entry.value >= 0) "+ R$ %.2f".format(entry.value) else "- R$ %.2f".format(entry.value.absoluteValue),
                        date = entry.date.format(DateTimeFormatter.ofPattern("dd/MM"))
                    )
                }
            )
        }.asLiveData()
    }

    fun stateOnceAndStream(): LiveData<HomeUiState> = _uiState

    suspend fun currentMonthExpenseExist(): Boolean {
        return (getMonthlyExpenseUseCase(YearMonth.now()).firstOrNull() != null)
    }

    fun deleteEntry(entry: Entry){
        viewModelScope.launch{
            deleteEntryUseCase.invoke(entry)
        }
    }
}
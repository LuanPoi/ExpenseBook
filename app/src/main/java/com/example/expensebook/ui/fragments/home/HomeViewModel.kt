package com.example.expensebook.ui.fragments.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.expensebook.data.data_source.local.entities.Entry
import com.example.expensebook.domain.usecase.DeleteEntryUseCase
import com.example.expensebook.domain.usecase.FetchMonthDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
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
    private val deleteEntryUseCase: DeleteEntryUseCase
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
                    ((if(it.totalExpend.times(-1) <= 0) 0f else it.totalExpend) / it.remainingAmount).roundToInt(),
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

    fun deleteEntry(entry: Entry){
        viewModelScope.launch{
            deleteEntryUseCase.invoke(entry)
        }
    }
}
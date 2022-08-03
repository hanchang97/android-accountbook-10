package com.nimok97.accountbook.presentation.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nimok97.accountbook.common.printLog
import com.nimok97.accountbook.domain.model.CalendarData
import com.nimok97.accountbook.domain.model.History
import com.nimok97.accountbook.domain.usecase.GetAllHistoryByYearAndMonthUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val getAllHistoryByYearAndMonthUseCase: GetAllHistoryByYearAndMonthUseCase
) : ViewModel() {

    var currentYear = 0
    var currentMonth = 0
    var dayCount = 0
    var startDay = 0
    var preMonthDayCount = 0

    private val _errorEvent = MutableSharedFlow<Boolean>()
    val errorEvent = _errorEvent.asSharedFlow()

    private val _incomeTotalFlow = MutableStateFlow<Int>(0)
    val incomeTotalFlow: StateFlow<Int> = _incomeTotalFlow

    private val _expenditureTotalFlow = MutableStateFlow<Int>(0)
    val expenditureTotalFlow: StateFlow<Int> = _expenditureTotalFlow

    private val _totalFlow = MutableStateFlow<Int>(0)
    val totalFlow: StateFlow<Int> = _totalFlow

    private val _calendarDataListFlow = MutableStateFlow<List<CalendarData>>(emptyList())
    val calendarDataListFlow: StateFlow<List<CalendarData>> = _calendarDataListFlow

    fun getHistoryitemList() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = getAllHistoryByYearAndMonthUseCase.getAllHistoryByYearAndMonth(
                currentYear,
                currentMonth
            )
            when {
                result.isSuccess -> {
                    val resultList = result.getOrNull()
                    resultList?.let {
                        setCalendar(it)
                    }
                }
                result.isFailure -> {
                    _errorEvent.emit(true)
                }
            }
        }
    }

    suspend fun setCalendar(historyList: List<History>) {
        var preDayCount = if (startDay == 7) 0 else startDay
        var nextDayCount = 42 - preDayCount - dayCount
        printLog("pre : $preDayCount, current : $dayCount, next : $nextDayCount, preMontDayCount : $preMonthDayCount")

        val calendarList = mutableListOf<CalendarData>()

        (1..dayCount).forEach {
            calendarList.add(
                CalendarData(0, 0, 0, currentYear, currentMonth, it, true)
            )
        }

        var incomeTotal = 0
        var expenditureTotal = 0

        historyList.forEach {
            val day = it.dayNum
            when (it.type) {
                0 -> {
                    calendarList[day - 1].income += it.amount
                    calendarList[day - 1].total += it.amount
                    incomeTotal += it.amount
                }
                else -> {
                    calendarList[day - 1].expenditure += it.amount
                    calendarList[day - 1].total -= it.amount
                    expenditureTotal += it.amount
                }
            }
        }

        _incomeTotalFlow.value = incomeTotal
        _expenditureTotalFlow.value = expenditureTotal
        _totalFlow.value = incomeTotal - expenditureTotal

        (1..preDayCount).forEach {
            calendarList.add(
                0, CalendarData(
                    0, 0, 0,
                    0, 0, preMonthDayCount - it + 1, false
                )
            )
        }

        (1..nextDayCount).forEach {
            calendarList.add(
                CalendarData(0, 0, 0, 0, 0, it, false)
            )
        }

        printLog("list size : ${calendarList.size}")
        calendarList.forEach {
            printLog("$it")
        }

        withContext(Dispatchers.Main) {
            _calendarDataListFlow.value = calendarList
        }
    }
}
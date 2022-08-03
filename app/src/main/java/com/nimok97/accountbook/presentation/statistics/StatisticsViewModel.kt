package com.nimok97.accountbook.presentation.statistics

import androidx.lifecycle.ViewModel
import com.nimok97.accountbook.domain.usecase.GetAllHistoryByYearAndMonthUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class StatisticsViewModel @Inject constructor(
    private val getAllHistoryByYearAndMonthUseCase: GetAllHistoryByYearAndMonthUseCase
) : ViewModel() {

    var currentYear = 0
    var currentMonth = 0

    fun setYearAndMonth(year: Int, month: Int) {
        currentYear = year
        currentMonth = month
    }

}
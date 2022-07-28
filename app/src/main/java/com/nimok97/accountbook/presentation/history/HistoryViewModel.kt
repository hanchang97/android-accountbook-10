package com.nimok97.accountbook.presentation.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nimok97.accountbook.data.dao.HistoryDao
import com.nimok97.accountbook.domain.usecase.AddHistoryUseCase
import com.nimok97.accountbook.domain.usecase.GetAllHistoryByYearAndMonthUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val addHistoryUseCase: AddHistoryUseCase,
    private val getAllHistoryByYearAndMonthUseCase: GetAllHistoryByYearAndMonthUseCase
) : ViewModel() {

    fun addHistory(historyDao: HistoryDao) {
        viewModelScope.launch(Dispatchers.IO) {
            addHistoryUseCase.addHistory(historyDao)
        }
    }

    fun getAllHistoryByYearAndMonth(year: Int, month: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            getAllHistoryByYearAndMonthUseCase.getAllHistoryByYearAndMonth(year, month)
        }
    }

}
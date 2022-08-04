package com.nimok97.accountbook.presentation.statistics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nimok97.accountbook.common.printLog
import com.nimok97.accountbook.domain.model.CategoryStatistics
import com.nimok97.accountbook.domain.model.History
import com.nimok97.accountbook.domain.usecase.GetAllHistoryByYearAndMonthUseCase
import com.nimok97.accountbook.domain.usecase.GetCategoryByIdUseCase
import com.nimok97.accountbook.presentation.util.CATEGORY_EXPENDITURE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.round

@HiltViewModel
class StatisticsViewModel @Inject constructor(
    private val getAllHistoryByYearAndMonthUseCase: GetAllHistoryByYearAndMonthUseCase,
    private val getCategoryByIdUseCase: GetCategoryByIdUseCase
) : ViewModel() {

    var currentYear = 0
    var currentMonth = 0

    private val _errorEvent = MutableSharedFlow<Boolean>()
    val errorEvent = _errorEvent.asSharedFlow()

    private val _expenditureTotalFlow = MutableStateFlow<Int>(0)
    val expenditureTotalFlow: StateFlow<Int> = _expenditureTotalFlow

    var historyExpenditureItemList = listOf<History>()

    val categoryMap = mutableMapOf<Int, Int>()

    fun setYearAndMonth(year: Int, month: Int) {
        currentYear = year
        currentMonth = month
    }

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
                        historyExpenditureItemList = it.filter { it.type == CATEGORY_EXPENDITURE }
                    }

                    var expenditure = 0
                    historyExpenditureItemList.forEach {
                        expenditure += it.amount
                    }
                    _expenditureTotalFlow.value = expenditure

                    setCategoryMap()
                }
                result.isFailure -> {
                    _errorEvent.emit(true)
                }
            }
        }
    }

    fun setCategoryMap() {
        categoryMap.clear()

        historyExpenditureItemList.forEach {
            val categoryId = it.categoryId
            if (categoryMap[categoryId] == null) categoryMap[categoryId] = 0
            categoryMap[categoryId] = categoryMap[categoryId]!! + it.amount
        }

        categoryMap.map {
            printLog("$it")
        }
        setCategoryStatisticsList()
    }

    fun setCategoryStatisticsList() {
        viewModelScope.launch(Dispatchers.IO) {
            val categoryStatisticsList = ArrayList<CategoryStatistics>()
            categoryMap.map {
                async {
                    val total = _expenditureTotalFlow.value
                    val categoryId = it.key
                    val amount = it.value
                    val result = getCategoryByIdUseCase.getCategoryById(categoryId)
                    when {
                        result.isSuccess -> {
                            result.getOrNull()?.let {
                                categoryStatisticsList.add(
                                    CategoryStatistics(categoryId, it.content, it.color, amount,
                                    round(amount.toFloat() / total * 100) / 100)
                                )
                            }
                        }
                        else -> {}
                    }
                }
            }.awaitAll()

            categoryStatisticsList.forEach {
                printLog("$it")
            }
        }
    }

}
package com.nimok97.accountbook.presentation.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nimok97.accountbook.common.HISTORY_CONTENT
import com.nimok97.accountbook.common.HISTORY_HEADER
import com.nimok97.accountbook.common.printLog
import com.nimok97.accountbook.data.dao.HistoryDao
import com.nimok97.accountbook.domain.model.History
import com.nimok97.accountbook.domain.model.HistoryItem
import com.nimok97.accountbook.domain.usecase.*
import com.nimok97.accountbook.presentation.util.CATEGORY_EXPENDITURE
import com.nimok97.accountbook.presentation.util.CATEGORY_INCOME
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val addHistoryUseCase: AddHistoryUseCase,
    private val getAllHistoryByYearAndMonthUseCase: GetAllHistoryByYearAndMonthUseCase,
    private val getCategoryByIdUseCase: GetCategoryByIdUseCase,
    private val getMethodByIdUseCase: GetMethodByIdUseCase,
    private val deleteHistoryUseCase: DeleteHistoryUseCase
) : ViewModel() {

    private val _errorEvent = MutableSharedFlow<Boolean>()
    val errorEvent = _errorEvent.asSharedFlow()

    private val _emptyEvent = MutableSharedFlow<Boolean>()
    val emptyEvent = _emptyEvent.asSharedFlow()

    private val _historyItemListFlow = MutableStateFlow<List<HistoryItem>>(emptyList())
    val historyItemListFlow: StateFlow<List<HistoryItem>> = _historyItemListFlow

    var historyItemList = mutableListOf<HistoryItem>()

    private val _incomeTotalFlow = MutableStateFlow<Int>(0)
    val incomeTotalFlow: StateFlow<Int> = _incomeTotalFlow

    private val _expenditureTotalFlow = MutableStateFlow<Int>(0)
    val expenditureTotalFlow: StateFlow<Int> = _expenditureTotalFlow

    private val _incomeCheckedFlow = MutableStateFlow<Boolean>(true)
    val incomeCheckedFlow: StateFlow<Boolean> = _incomeCheckedFlow

    private val _expenditureCheckedFlow = MutableStateFlow<Boolean>(true)
    val expenditureCheckedFlow: StateFlow<Boolean> = _expenditureCheckedFlow

    lateinit var selectedHistoryForEdit: History

    val deleteIdSet = mutableSetOf<Int>()

    fun addOrRemoveDeleteId(id: Int): Int {
        if (deleteIdSet.contains(id)) deleteIdSet.remove(id)
        else deleteIdSet.add(id)
        return deleteIdSet.size
    }

    fun addHistory(historyDao: HistoryDao) {
        viewModelScope.launch(Dispatchers.IO) {
            addHistoryUseCase.addHistory(historyDao)
        }
    }

    fun getHistoryItemList(year: Int, month: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = getAllHistoryByYearAndMonthUseCase.getAllHistoryByYearAndMonth(year, month)
            when {
                result.isSuccess -> {
                    val resultList = result.getOrNull()
                    resultList?.let {
                        if (it.isEmpty()) {
                            printLog("수입/지출 내역이 없습니다")
                            withContext(Dispatchers.Main) {
                                _incomeTotalFlow.value = 0
                                _expenditureTotalFlow.value = 0
                                _emptyEvent.emit(true)
                            }
                        } else {
                            var incomeTotalTemp = 0
                            var expenditureTotalTemp = 0

                            val tempList = MutableList(it.size) { HistoryItem() }
                            (it.indices).map {
                                tempList[it].history = resultList[it]
                                tempList[it].isLastItem = false
                                tempList[it].viewType = HISTORY_CONTENT

                                when (resultList[it].type) {
                                    0 -> incomeTotalTemp += resultList[it].amount
                                    else -> expenditureTotalTemp += resultList[it].amount
                                }

                                async {
                                    val category =
                                        getCategoryByIdUseCase.getCategoryById(resultList[it].categoryId)
                                    when {
                                        category.isSuccess -> {
                                            tempList[it].category = category.getOrNull()
                                        }
                                    }
                                }

                                async {
                                    val method =
                                        getMethodByIdUseCase.getMethodById(resultList[it].methodId)
                                    when {
                                        method.isSuccess -> {
                                            tempList[it].method = method.getOrNull()
                                        }
                                    }
                                }

                            }.awaitAll()

                            tempList.forEach {
                                printLog("$it")
                            }

                            _incomeTotalFlow.value = incomeTotalTemp
                            _expenditureTotalFlow.value = expenditureTotalTemp

                            historyItemList = tempList
                            filterHistoryItemList()
                        }
                    }
                }
                result.isFailure -> {
                    _errorEvent.emit(true)
                }
            }
        }
    }

    fun clickIncomeFilter() {
        viewModelScope.launch {
            _incomeCheckedFlow.value = !_incomeCheckedFlow.value
            filterHistoryItemList()
        }
    }

    fun clickExpenditureFilter() {
        viewModelScope.launch {
            _expenditureCheckedFlow.value = !expenditureCheckedFlow.value
            filterHistoryItemList()
        }
    }

    suspend fun filterHistoryItemList() {
        var filteredList = listOf<HistoryItem>()
        if (_incomeCheckedFlow.value && _expenditureCheckedFlow.value) {
            filteredList = historyItemList
            convertHistoryItemList(filteredList)
        } else if (_incomeCheckedFlow.value && !_expenditureCheckedFlow.value) {
            filteredList = historyItemList.filter { it.history!!.type == CATEGORY_INCOME }
            convertHistoryItemList(filteredList)
        } else if (!_incomeCheckedFlow.value && _expenditureCheckedFlow.value) {
            filteredList = historyItemList.filter { it.history!!.type == CATEGORY_EXPENDITURE }
            convertHistoryItemList(filteredList)
        } else {
            _emptyEvent.emit(true)
        }
    }

    suspend fun convertHistoryItemList(tempList: List<HistoryItem>) {
        _emptyEvent.emit(false)
        val historyItemListConverted = mutableListOf<HistoryItem>()
        var sumIncome = 0
        var sumExpenditure = 0
        var headerInx = 0
        tempList.forEachIndexed { index, historyItem ->
            if (index == 0) {
                historyItemListConverted.add(HistoryItem(HISTORY_HEADER, historyItem.history))
            }

            if (index == tempList.size - 1) {
                historyItemListConverted.add(
                    HistoryItem(
                        HISTORY_CONTENT,
                        historyItem.history,
                        historyItem.category,
                        historyItem.method,
                        isLastItem = true
                    )
                )

                when (historyItem.history!!.type) {
                    0 -> sumIncome += historyItem.history!!.amount
                    else -> sumExpenditure += historyItem.history!!.amount
                }

                historyItemListConverted.get(headerInx).income = sumIncome
                historyItemListConverted.get(headerInx).expenditure = sumExpenditure
            } else {
                val curDay = historyItem.history!!.dayNum
                val nextDay = tempList[index + 1].history!!.dayNum

                when (historyItem.history!!.type) {
                    0 -> sumIncome += historyItem.history!!.amount
                    else -> sumExpenditure += historyItem.history!!.amount
                }

                if (curDay == nextDay) {
                    printLog("$curDay $nextDay")
                    historyItemListConverted.add(historyItem)
                } else { // 다음 원소 값으로 헤더 넣어주기
                    historyItemListConverted.add(
                        HistoryItem(
                            HISTORY_CONTENT, historyItem.history, historyItem.category,
                            historyItem.method, true
                        )
                    )

                    historyItemListConverted.get(headerInx).income = sumIncome
                    historyItemListConverted.get(headerInx).expenditure = sumExpenditure
                    sumIncome = 0
                    sumExpenditure = 0

                    historyItemListConverted.add(HistoryItem(HISTORY_HEADER, tempList[index + 1].history))
                    headerInx = historyItemListConverted.size - 1
                }
            }
        }

        printLog("데이터 조회 및 사용가능 형태로 변환")
        historyItemListConverted.forEach {
            printLog("$it")
        }

        withContext(Dispatchers.Main) {
            if (historyItemListConverted.size == 0) {
                _emptyEvent.emit(true)
            }
            _historyItemListFlow.value = historyItemListConverted
        }
    }

    fun deleteHistories(year: Int, month: Int) {
        printLog("$deleteIdSet")
        viewModelScope.launch(Dispatchers.IO) {
            deleteIdSet.map {
                async {
                    val result = deleteHistoryUseCase.deleteHistory(it)
                }
            }.awaitAll()

            getHistoryItemList(year, month)
            deleteIdSet.clear()
        }
    }
}
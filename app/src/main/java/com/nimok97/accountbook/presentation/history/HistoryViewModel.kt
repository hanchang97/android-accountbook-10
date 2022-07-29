package com.nimok97.accountbook.presentation.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nimok97.accountbook.common.printLog
import com.nimok97.accountbook.data.dao.HistoryDao
import com.nimok97.accountbook.domain.model.HistoryItem
import com.nimok97.accountbook.domain.usecase.AddHistoryUseCase
import com.nimok97.accountbook.domain.usecase.GetAllHistoryByYearAndMonthUseCase
import com.nimok97.accountbook.domain.usecase.GetCategoryByIdUseCase
import com.nimok97.accountbook.domain.usecase.GetMethodByIdUseCase
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
    private val getMethodByIdUseCase: GetMethodByIdUseCase
) : ViewModel() {

    private val _errorEvent = MutableSharedFlow<Boolean>()
    val errorEvent = _errorEvent.asSharedFlow()

    private val _historyItemListFlow = MutableStateFlow<List<HistoryItem>>(emptyList())
    val historyItemListFlow: StateFlow<List<HistoryItem>> = _historyItemListFlow

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
                        } else {
                            val tempList = MutableList(it.size) { HistoryItem() }
                            (it.indices).map {
                                tempList[it].history = resultList[it]
                                tempList[it].isLastItem = false
                                tempList[it].viewType = "content"

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

                            convertHistoryItemList(tempList)
                        }
                    }
                }
                result.isFailure -> {
                    _errorEvent.emit(true)
                }
            }
        }
    }

    suspend fun convertHistoryItemList(tempList: MutableList<HistoryItem>) {
        val historyItemList = mutableListOf<HistoryItem>()
        tempList.forEachIndexed { index, historyItem ->
            if (index == 0) {
                historyItemList.add(HistoryItem("header", historyItem.history))
            }

            if (index == tempList.size - 1) {
                historyItemList.add(
                    HistoryItem(
                        "content",
                        historyItem.history,
                        historyItem.category,
                        historyItem.method,
                        isLastItem = true
                    )
                )
            } else {
                val curDay = historyItem.history!!.dayNum
                val nextDay = tempList[index + 1].history!!.dayNum

                if (curDay == nextDay) {
                    printLog("$curDay $nextDay")
                    historyItemList.add(historyItem)
                } else { // 다음 원소 값으로 헤더 넣어주기
                    historyItemList.add(
                        HistoryItem(
                            "content", historyItem.history, historyItem.category,
                            historyItem.method, true
                        )
                    )
                    historyItemList.add(HistoryItem("header", tempList[index + 1].history))
                }
            }
        }

        printLog("데이터 조회 및 사용가능 형태로 변환")
        historyItemList.forEach {
            printLog("$it")
        }

        _historyItemListFlow.value = historyItemList
    }
}
package com.nimok97.accountbook.presentation.history.manage.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nimok97.accountbook.common.defaultDateString
import com.nimok97.accountbook.common.getDateString
import com.nimok97.accountbook.common.printLog
import com.nimok97.accountbook.data.dao.HistoryDao
import com.nimok97.accountbook.domain.model.Category
import com.nimok97.accountbook.domain.model.History
import com.nimok97.accountbook.domain.model.Method
import com.nimok97.accountbook.domain.usecase.GetAllCategoryUseCase
import com.nimok97.accountbook.domain.usecase.GetAllMethodUseCase
import com.nimok97.accountbook.domain.usecase.UpdateHistoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditHistoryViewModel @Inject constructor(
    private val getAllCategoryUseCase: GetAllCategoryUseCase,
    private val getAllMethodUseCase: GetAllMethodUseCase,
    private val updateHistoryUseCase: UpdateHistoryUseCase
) : ViewModel() {

    var id = -1
    var categoryType = 0 // 0 = 수입, 1 = 지출
    var selectedMethodId = -1
    var selectedCategoryId = -1
    var year = 0
    var month = 0
    var dayNum = 0
    var dayStr = ""
    var amount = 0
    var content = "미입력" // 선택 사항

    private val _dateStringFlow = MutableStateFlow<String>(getDateString(year, month, dayNum, dayStr))
    val dateStringFlow: StateFlow<String> = _dateStringFlow

    private val _dateClickedEvent = MutableSharedFlow<Boolean>()
    val dateClickedEvent = _dateClickedEvent.asSharedFlow()

    private val _buttonActiveFlow = MutableStateFlow<Boolean>(false)
    val buttonActiveFlow: StateFlow<Boolean> = _buttonActiveFlow

    private val _updateHistorySuccessful = MutableSharedFlow<Boolean>()
    val updateHistorySuccessful = _updateHistorySuccessful.asSharedFlow()

    private val _methodListFlow = MutableStateFlow<List<Method>>(emptyList())
    val methodistFlow: StateFlow<List<Method>> = _methodListFlow

    private val _categoryIncomeListFlow = MutableStateFlow<List<Category>>(emptyList())
    val categoryIncomeListFlow: StateFlow<List<Category>> = _categoryIncomeListFlow

    private val _categoryExpenditureListFlow = MutableStateFlow<List<Category>>(emptyList())
    val categoryExpenditureListFlow: StateFlow<List<Category>> = _categoryExpenditureListFlow

    private val _categoryListFlow = MutableStateFlow<List<Category>>(emptyList())
    val categoryListFlow: StateFlow<List<Category>> = _categoryListFlow

    fun setOrginData(selectedHistory: History) {
        id = selectedHistory.id
        categoryType = selectedHistory.type
        selectedMethodId = selectedHistory.methodId
        selectedCategoryId = selectedHistory.categoryId
        year = selectedHistory.year
        month = selectedHistory.month
        dayNum = selectedHistory.dayNum
        dayStr = selectedHistory.dayStr
        amount = selectedHistory.amount
        content = selectedHistory.content
        _dateStringFlow.value = getDateString(year, month, dayNum, dayStr)
    }

    fun dateClick() {
        viewModelScope.launch {
            _dateClickedEvent.emit(true)
        }
    }

    fun setDateString() {
        _dateStringFlow.value = getDateString(year, month, dayNum, dayStr)
    }

    fun checkData() {
        _buttonActiveFlow.value =
            selectedCategoryId >= 1 && _dateStringFlow.value != defaultDateString && amount > 0
    }

    fun getAllMethod() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = getAllMethodUseCase()
            when {
                result.isSuccess -> {
                    printLog("AddHistoryViewModel/ get all method success")
                    result.getOrNull()?.let {
                        it.forEach {
                            printLog("$it")
                        }
                        _methodListFlow.value = it
                    }
                }
                result.isFailure -> {
                    printLog("AddHistoryViewModel/ get all method fail")
                }
            }
        }
    }

    fun getAllCategory() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = getAllCategoryUseCase()
            when {
                result.isSuccess -> {
                    printLog("AddHistoryViewModel/ get all category success")
                    val incomeList = ArrayList<Category>()
                    val expenditureList = ArrayList<Category>()
                    result.getOrNull()?.let {
                        it.forEach {
                            printLog("$it")
                            if (it.type == 0) incomeList.add(it)
                            else expenditureList.add(it)
                        }
                        _categoryIncomeListFlow.value = incomeList
                        _categoryExpenditureListFlow.value = expenditureList
                        when(categoryType) {
                            0 -> _categoryListFlow.value = incomeList
                            else -> _categoryListFlow.value = expenditureList
                        }
                    }
                }
                result.isFailure -> {
                    printLog("AddHistoryViewModel/ get all category fail")
                }
            }
        }
    }

    fun updateHistory() {
        val historyDao = HistoryDao(
            categoryType,
            year,
            month,
            dayNum,
            dayStr,
            amount,
            content,
            selectedMethodId,
            selectedCategoryId
        )

        viewModelScope.launch(Dispatchers.IO) {
            printLog("$historyDao")
            val result = updateHistoryUseCase.updateHistory(id, historyDao)
            when {
                result.isSuccess -> {
                    _updateHistorySuccessful.emit(true)
                }
                result.isFailure -> {
                    _updateHistorySuccessful.emit(false)
                }
            }
        }
    }
}
package com.nimok97.accountbook.presentation.history.manage.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nimok97.accountbook.common.defaultDateString
import com.nimok97.accountbook.common.printLog
import com.nimok97.accountbook.data.dao.HistoryDao
import com.nimok97.accountbook.domain.model.Category
import com.nimok97.accountbook.domain.model.Method
import com.nimok97.accountbook.domain.usecase.AddHistoryUseCase
import com.nimok97.accountbook.domain.usecase.GetAllCategoryUseCase
import com.nimok97.accountbook.domain.usecase.GetAllMethodUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddHistoryViewModel @Inject constructor(
    private val getAllCategoryUseCase: GetAllCategoryUseCase,
    private val getAllMethodUseCase: GetAllMethodUseCase,
    private val addHistoryUseCase: AddHistoryUseCase
) : ViewModel() {

    var categoryType = 0 // 0 = 수입, 1 = 지출
    var selectedMethodId = -1
    var selectedCategoryId = -1
    var year = 0
    var month = 0
    var dayNum = 0
    var dayStr = ""
    var amount = 0
    var content = "미입력" // 선택 사항

    private val _incomeCheckedFlow = MutableStateFlow<Boolean>(true)
    val incomeCheckedFlow: StateFlow<Boolean> = _incomeCheckedFlow

    private val _expenditureCheckedFlow = MutableStateFlow<Boolean>(false)
    val expenditureCheckedFlow: StateFlow<Boolean> = _expenditureCheckedFlow

    private val _methodListFlow = MutableStateFlow<List<Method>>(emptyList())
    val methodistFlow: StateFlow<List<Method>> = _methodListFlow

    private val _categoryIncomeListFlow = MutableStateFlow<List<Category>>(emptyList())
    val categoryIncomeListFlow: StateFlow<List<Category>> = _categoryIncomeListFlow

    private val _categoryExpenditureListFlow = MutableStateFlow<List<Category>>(emptyList())
    val categoryExpenditureListFlow: StateFlow<List<Category>> = _categoryExpenditureListFlow

    private val _dateClickedEvent = MutableSharedFlow<Boolean>()
    val dateClickedEvent = _dateClickedEvent.asSharedFlow()

    private val _dateStringFlow = MutableStateFlow<String>(defaultDateString)
    val dateStringFlow: StateFlow<String> = _dateStringFlow

    private val _buttonActiveFlow = MutableStateFlow<Boolean>(false)
    val buttonActiveFlow: StateFlow<Boolean> = _buttonActiveFlow

    private val _addHistorySuccessful = MutableSharedFlow<Boolean>()
    val addHistorySuccessful = _addHistorySuccessful.asSharedFlow()

    fun selectIncome() {
        _incomeCheckedFlow.value = true
        _expenditureCheckedFlow.value = false
        categoryType = 0
        clearData()
    }

    fun selectExpenditure() {
        _incomeCheckedFlow.value = false
        _expenditureCheckedFlow.value = true
        categoryType = 1
        clearData()
    }

    fun dateClick() {
        viewModelScope.launch {
            _dateClickedEvent.emit(true)
        }
    }

    fun setDateString() {
        _dateStringFlow.value = "$year. $month. $dayNum ${dayStr}요일"
    }

    fun clearData() {
        selectedMethodId = -1
        selectedCategoryId = -1
        year = 0
        month = 0
        dayNum = 0
        dayStr = ""
        amount = 0
        content = "미입력"
        _dateStringFlow.value = defaultDateString
        checkData()
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
                    }
                }
                result.isFailure -> {
                    printLog("AddHistoryViewModel/ get all category fail")
                }
            }
        }
    }

    fun addHistory() {
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
            val result = addHistoryUseCase.addHistory(historyDao)
            when {
                result.isSuccess -> {
                    _addHistorySuccessful.emit(true)
                }
                result.isFailure -> {
                    _addHistorySuccessful.emit(false)
                }
            }
        }
    }

}
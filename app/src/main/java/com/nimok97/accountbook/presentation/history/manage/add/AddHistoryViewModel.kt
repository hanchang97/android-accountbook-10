package com.nimok97.accountbook.presentation.history.manage.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nimok97.accountbook.common.printLog
import com.nimok97.accountbook.domain.model.Category
import com.nimok97.accountbook.domain.model.Method
import com.nimok97.accountbook.domain.usecase.GetAllCategoryUseCase
import com.nimok97.accountbook.domain.usecase.GetAllMethodUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddHistoryViewModel @Inject constructor(
    private val getAllCategoryUseCase: GetAllCategoryUseCase,
    private val getAllMethodUseCase: GetAllMethodUseCase
): ViewModel() {

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

    private val _buttonActiveFlow = MutableStateFlow<Boolean>(false)
    val buttonActiveFlow: StateFlow<Boolean> = _buttonActiveFlow

    fun selectIncome(){
        _incomeCheckedFlow.value = true
        _expenditureCheckedFlow.value = false
        categoryType = 0
        clearData()
    }

    fun selectExpenditure(){
        _incomeCheckedFlow.value = false
        _expenditureCheckedFlow.value = true
        categoryType = 1
        clearData()
    }

    fun clearData(){
        selectedMethodId = -1
        selectedCategoryId = -1
        year = 0
        month = 0
        dayNum = 0
        dayStr = ""
        amount = 0
        content = "미입력"
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

}
package com.nimok97.accountbook.presentation.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nimok97.accountbook.common.printLog
import com.nimok97.accountbook.data.dao.CategoryDao
import com.nimok97.accountbook.data.dao.MethodDao
import com.nimok97.accountbook.domain.model.Category
import com.nimok97.accountbook.domain.model.Method
import com.nimok97.accountbook.domain.usecase.AddCategoryUseCase
import com.nimok97.accountbook.domain.usecase.AddMethodUseCase
import com.nimok97.accountbook.domain.usecase.GetAllCategoryUseCase
import com.nimok97.accountbook.domain.usecase.GetAllMethodUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val addCategoryUseCase: AddCategoryUseCase,
    private val getAllCategoryUseCase: GetAllCategoryUseCase,
    private val addMethodUseCase: AddMethodUseCase,
    private val getAllMethodUseCase: GetAllMethodUseCase
) : ViewModel() {

    private val _methodListFlow = MutableStateFlow<List<Method>>(emptyList())
    val methodistFlow: StateFlow<List<Method>> = _methodListFlow

    private val _categoryIncomeListFlow = MutableStateFlow<List<Category>>(emptyList())
    val categoryIncomeListFlow: StateFlow<List<Category>> = _categoryIncomeListFlow

    private val _categoryExpenditureListFlow = MutableStateFlow<List<Category>>(emptyList())
    val categoryExpenditureListFlow: StateFlow<List<Category>> = _categoryExpenditureListFlow

    fun addCategory(categoryDao: CategoryDao) {
        viewModelScope.launch(Dispatchers.IO) {
            addCategoryUseCase(categoryDao)
        }
    }

    fun getAllCategory() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = getAllCategoryUseCase()
            when {
                result.isSuccess -> {
                    printLog("get all category success")
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
                    printLog("get all category fail")
                }
            }
        }
    }

    fun addMethod(methodDao: MethodDao) {
        viewModelScope.launch(Dispatchers.IO) {
            addMethodUseCase.addMethod(methodDao)
        }
    }

    fun getAllMethod() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = getAllMethodUseCase()
            when {
                result.isSuccess -> {
                    printLog("get all method success")
                    result.getOrNull()?.let {
                        it.forEach {
                            printLog("$it")
                        }
                        _methodListFlow.value = it
                    }
                }
                result.isFailure -> {
                    printLog("get all method fail")
                }
            }
        }
    }
}
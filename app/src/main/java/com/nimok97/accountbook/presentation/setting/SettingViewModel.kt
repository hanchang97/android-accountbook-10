package com.nimok97.accountbook.presentation.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nimok97.accountbook.data.dao.CategoryDao
import com.nimok97.accountbook.data.dao.MethodDao
import com.nimok97.accountbook.domain.model.Category
import com.nimok97.accountbook.domain.usecase.AddCategoryUseCase
import com.nimok97.accountbook.domain.usecase.AddMethodUseCase
import com.nimok97.accountbook.domain.usecase.GetAllCategoryUseCase
import com.nimok97.accountbook.domain.usecase.GetAllMethodUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val addCategoryUseCase: AddCategoryUseCase,
    private val getAllCategoryUseCase: GetAllCategoryUseCase,
    private val addMethodUseCase: AddMethodUseCase,
    private val getAllMethodUseCase: GetAllMethodUseCase
) : ViewModel() {

    fun addCategory(categoryDao: CategoryDao) {
        viewModelScope.launch(Dispatchers.IO) {
            addCategoryUseCase.addCategory(categoryDao)
        }
    }

    fun getAllCategory() {
        viewModelScope.launch(Dispatchers.IO) {
            getAllCategoryUseCase.getAllCategory()
        }
    }

    fun addMethod(methodDao: MethodDao) {
        viewModelScope.launch(Dispatchers.IO) {
            addMethodUseCase.addMethod(methodDao)
        }
    }

    fun getAllMethod(){
        viewModelScope.launch(Dispatchers.IO) {
            getAllMethodUseCase.getAllMethod()
        }
    }
}
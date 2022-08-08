package com.nimok97.accountbook.presentation.setting.income

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nimok97.accountbook.common.printLog
import com.nimok97.accountbook.data.dao.CategoryDao
import com.nimok97.accountbook.domain.usecase.AddCategoryUseCase
import com.nimok97.accountbook.domain.usecase.CheckCategoryExistenceByContentUseCase
import com.nimok97.accountbook.presentation.util.CATEGORY_INCOME
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class IncomeCategoryViewModel @Inject constructor(
    private val checkCategoryExistenceByContentUseCase: CheckCategoryExistenceByContentUseCase,
    private val addCategoryUseCase: AddCategoryUseCase
) : ViewModel() {
    private var content = ""
    private var selectedColorString = "#9BD182"
    private val type = CATEGORY_INCOME

    private val _contentFlow = MutableStateFlow<Boolean>(false)
    val contentFlow: StateFlow<Boolean> = _contentFlow

    private val _contentAlreadyExist = MutableSharedFlow<Boolean>()
    val contentAlreadyExist = _contentAlreadyExist.asSharedFlow()

    private val _addCategorySuccessful = MutableSharedFlow<Boolean>()
    val addCategorySuccessful = _addCategorySuccessful.asSharedFlow()

    fun checkContentEmpty(content: String) {
        this.content = content
        _contentFlow.value = this.content.isNotEmpty()
    }

    fun setCurrentColor(selectedColor: String) {
        selectedColorString = selectedColor
    }

    fun checkCategoryExistenceByContent() {
        printLog("content : $content, color : $selectedColorString")
        viewModelScope.launch(Dispatchers.IO) {
            val result = checkCategoryExistenceByContentUseCase(content)
            when {
                result.isSuccess -> {
                    result.getOrNull()?.let {
                        if (it) {
                            _contentAlreadyExist.emit(true)
                        } else {
                            printLog("$content not exist")
                            addCategory(CategoryDao(type, content, selectedColorString))
                        }
                    }
                }
                result.isFailure -> {
                    printLog("check category fail")
                }
            }
        }
    }

    suspend fun addCategory(categoryDao: CategoryDao) {
        val result = addCategoryUseCase.invoke(categoryDao)
        when {
            result.isSuccess -> {
                _addCategorySuccessful.emit(true)
            }
            result.isFailure -> {
                _addCategorySuccessful.emit(false)
            }
        }
    }
}
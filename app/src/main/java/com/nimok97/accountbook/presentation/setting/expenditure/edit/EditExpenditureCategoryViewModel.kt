package com.nimok97.accountbook.presentation.setting.expenditure.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nimok97.accountbook.common.printLog
import com.nimok97.accountbook.data.dao.CategoryDao
import com.nimok97.accountbook.domain.usecase.CheckCategoryExistenceByContentUseCase
import com.nimok97.accountbook.domain.usecase.UpdateCategoryUseCase
import com.nimok97.accountbook.presentation.util.CATEGORY_EXPENDITURE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditExpenditureCategoryViewModel @Inject constructor(
    private val checkCategoryExistenceByContentUseCase: CheckCategoryExistenceByContentUseCase,
    private val updateCategoryUseCase: UpdateCategoryUseCase
) : ViewModel() {

    var curerntCategoryId = -1
    var selectedContent = ""  // 색상만 수정하는 경우를 처리하기 위함
    var content = ""
    var selectedColorString = "#4A6CC3"
    private val type = CATEGORY_EXPENDITURE

    private val _contentFlow = MutableStateFlow<Boolean>(true)
    val contentFlow: StateFlow<Boolean> = _contentFlow

    private val _contentAlreadyExist = MutableSharedFlow<Boolean>()
    val contentAlreadyExist = _contentAlreadyExist.asSharedFlow()

    private val _updateCategorySuccessful = MutableSharedFlow<Boolean>()
    val updateCategorySuccessful = _updateCategorySuccessful.asSharedFlow()

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
            if(content == selectedContent) {
                updateCategory(curerntCategoryId, CategoryDao(type, content, selectedColorString))
            } else {
                val result = checkCategoryExistenceByContentUseCase(content)
                when {
                    result.isSuccess -> {
                        result.getOrNull()?.let {
                            if (it) {
                                _contentAlreadyExist.emit(true)
                            } else {
                                printLog("$content not exist")
                                updateCategory(curerntCategoryId, CategoryDao(type, content, selectedColorString))
                            }
                        }
                    }
                    result.isFailure -> {
                        printLog("check category fail")
                    }
                }
            }
        }
    }

    suspend fun updateCategory(id: Int, categoryDao: CategoryDao) {
        val result = updateCategoryUseCase(id, categoryDao)
        when {
            result.isSuccess -> {
                _updateCategorySuccessful.emit(true)
            }
            result.isFailure -> {
                _updateCategorySuccessful.emit(false)
            }
        }
    }
}
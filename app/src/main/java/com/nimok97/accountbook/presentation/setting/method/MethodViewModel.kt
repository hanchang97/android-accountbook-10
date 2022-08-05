package com.nimok97.accountbook.presentation.setting.method

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nimok97.accountbook.common.printLog
import com.nimok97.accountbook.data.dao.MethodDao
import com.nimok97.accountbook.domain.usecase.AddMethodUseCase
import com.nimok97.accountbook.domain.usecase.CheckMethodExistenceByContentUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MethodViewModel @Inject constructor(
    private val checkMethodExistenceByContentUseCase: CheckMethodExistenceByContentUseCase,
    private val addMethodUseCase: AddMethodUseCase
) :
    ViewModel() {

    private var content = ""

    private val _contentFlow = MutableStateFlow<Boolean>(false)
    val contentFlow: StateFlow<Boolean> = _contentFlow

    private val _contentAlreadyExist = MutableSharedFlow<Boolean>()
    val contentAlreadyExist = _contentAlreadyExist.asSharedFlow()

    private val _addMethodSuccessful = MutableSharedFlow<Boolean>()
    val addMethodSuccessful = _addMethodSuccessful.asSharedFlow()

    fun checkMethodExistenceByContent() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = checkMethodExistenceByContentUseCase(content)
            when {
                result.isSuccess -> {
                    result.getOrNull()?.let {
                        if (it) {
                            _contentAlreadyExist.emit(true)
                        } else {
                            printLog("$content not exist")
                            addMethod(content)
                        }
                    }
                }
                result.isFailure -> {
                    printLog("check method fail")
                }
            }
        }
    }

    suspend fun addMethod(content: String) {
        val result = addMethodUseCase.invoke(MethodDao(content))
        when {
            result.isSuccess -> {
                _addMethodSuccessful.emit(true)
            }
            result.isFailure -> {
                _addMethodSuccessful.emit(false)
            }
        }
    }

    fun checkContentEmpty(content: String) {
        this.content = content
        _contentFlow.value = this.content.isNotEmpty()
    }
}
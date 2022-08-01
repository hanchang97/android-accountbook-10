package com.nimok97.accountbook.presentation.setting.method

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nimok97.accountbook.common.printLog
import com.nimok97.accountbook.domain.usecase.CheckMethodExistenceByContentUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MethodViewModel @Inject constructor(private val checkMethodExistenceByContentUseCase: CheckMethodExistenceByContentUseCase) :
    ViewModel() {

    private var content = ""

    private val _contentFlow = MutableStateFlow<Boolean>(false)
    val contentFlow: StateFlow<Boolean> = _contentFlow

    fun checkMethodExistenceByContent(content: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = checkMethodExistenceByContentUseCase(content)
            when {
                result.isSuccess -> {
                    result.getOrNull()?.let {
                        if (it) {
                            printLog("$content exist")
                        } else {
                            printLog("$content not exist")
                        }
                    }
                }
                result.isFailure -> {
                    printLog("check method fail")
                }
            }
        }
    }

    fun checkContentEmpty(content: String) {
        this.content = content
        _contentFlow.value = this.content.isNotEmpty()
    }
}
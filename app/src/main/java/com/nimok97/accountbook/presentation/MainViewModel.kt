package com.nimok97.accountbook.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(): ViewModel() {
    private val _fabClickedEvent = MutableSharedFlow<Boolean>()
    val fabClickedEvent = _fabClickedEvent.asSharedFlow()

    fun fabClick(){
        viewModelScope.launch {
            _fabClickedEvent.emit(true)
        }
    }
}
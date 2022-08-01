package com.nimok97.accountbook.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {
    private val _fabClickedEvent = MutableSharedFlow<Boolean>()
    val fabClickedEvent = _fabClickedEvent.asSharedFlow()

    private val _moveToMethodFragmentEvent = MutableSharedFlow<Boolean>()
    val moveToMethodFragmentEvent = _moveToMethodFragmentEvent.asSharedFlow()

    private val _backEventInMethodFragment = MutableSharedFlow<Boolean>()
    val backEventInMethodFragment = _backEventInMethodFragment.asSharedFlow()

    private val _moveToExpenditureCategoryFragmentEvent = MutableSharedFlow<Boolean>()
    val moveToExpenditureCategoryFragmentEvent = _moveToExpenditureCategoryFragmentEvent.asSharedFlow()

    private val _backButtonPressedEvent = MutableSharedFlow<Boolean>()
    val backButtonPressedEvent = _backButtonPressedEvent.asSharedFlow()

    fun fabClick() {
        viewModelScope.launch {
            _fabClickedEvent.emit(true)
        }
    }

    fun moveToMethodFragment() {
        viewModelScope.launch {
            _moveToMethodFragmentEvent.emit(true)
        }
    }

    fun pressBackInMethodFragment() {
        viewModelScope.launch {
            _backEventInMethodFragment.emit(true)
        }
    }

    fun moveToExpenditureCategoryFragment() {
        viewModelScope.launch {
            _moveToExpenditureCategoryFragmentEvent.emit(true)
        }
    }

    fun pressBackButtonInAppBar() {
        viewModelScope.launch {
            _backButtonPressedEvent.emit(true)
        }
    }

}
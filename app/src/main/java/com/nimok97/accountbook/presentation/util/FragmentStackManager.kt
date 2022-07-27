package com.nimok97.accountbook.presentation.util

import androidx.fragment.app.Fragment
import java.util.*

object FragmentStackManager {

    // 현재 상태 : 탭 전환 시 다른 탭의 스택도 clear

    private val stackTabHistory = ArrayList<Fragment>()
    private val stackTabCalendar = ArrayList<Fragment>()
    private val stackTabStatistics = ArrayList<Fragment>()
    private val stackTabSetting = ArrayList<Fragment>()

    fun pushStack(tabIndex: Int, tag: Fragment) {
        when (tabIndex) {
            0 -> stackTabHistory.add(tag)
            1 -> stackTabCalendar.add(tag)
            2 -> stackTabStatistics.add(tag)
            else -> stackTabSetting.add(tag)
        }
    }

    fun popStack(tabIndex: Int) {
        when (tabIndex) {
            0 -> stackTabHistory.removeLast()
            1 -> stackTabCalendar.removeLast()
            2 -> stackTabStatistics.removeLast()
            else -> stackTabSetting.removeLast()
        }
    }

    fun getTopStack(tabIndex: Int): Fragment {
        return when (tabIndex) {
            0 -> stackTabHistory.last()
            1 -> stackTabCalendar.last()
            2 -> stackTabStatistics.last()
            else -> stackTabSetting.last()
        }
    }

    fun getStackSize(tabIndex: Int): Int {
        return when (tabIndex) {
            0 -> stackTabHistory.size
            1 -> stackTabCalendar.size
            2 -> stackTabStatistics.size
            else -> stackTabSetting.size
        }
    }

    fun clearAllStack() {
        stackTabHistory.clear()
        stackTabCalendar.clear()
        stackTabStatistics.clear()
        stackTabSetting.clear()
    }
}
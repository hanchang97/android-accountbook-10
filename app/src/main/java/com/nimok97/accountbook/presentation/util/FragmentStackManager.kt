package com.nimok97.accountbook.presentation.util

import java.util.*

object FragmentStackManager{

    // 현재 상태 : 탭 전환 시 다른 탭의 스택도 clear

    private val stackTabHistory = ArrayList<String>()
    private val stackTabCalendar = ArrayList<String>()
    private val stackTabStatistics = ArrayList<String>()
    private val stackTabSetting = ArrayList<String>()

    fun pushStack(tabIndex: Int, tag: String){
        when (tabIndex){
            0 -> stackTabHistory.add(tag)
            1 -> stackTabCalendar.add(tag)
            2 -> stackTabStatistics.add(tag)
            else -> stackTabSetting.add(tag)
        }
    }

    fun popStack(tabIndex: Int){
        when (tabIndex){
            0 -> stackTabHistory.removeLast()
            1 -> stackTabCalendar.removeLast()
            2 -> stackTabStatistics.removeLast()
            else -> stackTabSetting.removeLast()
        }
    }

    fun getTopStack(tabIndex: Int, tag: String): String{
        return when (tabIndex){
            0 -> stackTabHistory.last()
            1 -> stackTabCalendar.last()
            2 -> stackTabStatistics.last()
            else -> stackTabSetting.last()
        }
    }

    fun clearAllStack(){
        stackTabHistory.clear()
        stackTabCalendar.clear()
        stackTabStatistics.clear()
        stackTabSetting.clear()
    }
}
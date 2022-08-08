package com.nimok97.accountbook.presentation.util

import androidx.fragment.app.Fragment
import com.nimok97.accountbook.domain.model.Method
import com.nimok97.accountbook.presentation.calendar.CalendarFragment
import com.nimok97.accountbook.presentation.history.HistoryFragment
import com.nimok97.accountbook.presentation.history.manage.add.AddHistoryFragment
import com.nimok97.accountbook.presentation.history.manage.edit.EditHistoryFragment
import com.nimok97.accountbook.presentation.setting.SettingFragment
import com.nimok97.accountbook.presentation.setting.expenditure.ExpenditureCategoryFragment
import com.nimok97.accountbook.presentation.setting.expenditure.edit.EditExpenditureCategoryFragment
import com.nimok97.accountbook.presentation.setting.income.IncomeCategoryFragment
import com.nimok97.accountbook.presentation.setting.method.MethodFragment
import com.nimok97.accountbook.presentation.statistics.StatisticsFragment
import java.util.*

object FragmentStackManager {

    // 현재 상태 : 탭 전환 시 다른 탭의 스택도 clear

    private val stackTabHistory = ArrayList<String>()
    private val stackTabCalendar = ArrayList<String>()
    private val stackTabStatistics = ArrayList<String>()
    private val stackTabSetting = ArrayList<String>()

    fun pushStack(tabIndex: Int, tag: String) {
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

    fun getTopStack(tabIndex: Int): String {
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

    fun returnFragmentByTag(tag: String): Fragment {
        return when(tag) {
            TAG_HISTORY -> HistoryFragment()
            TAG_HISTORY_ADD -> AddHistoryFragment()
            TAG_HISTORY_EDIT -> EditHistoryFragment()
            TAG_CALENDAR -> CalendarFragment()
            TAG_STATISTICS -> StatisticsFragment()
            TAG_SETTING -> SettingFragment()
            TAG_SETTING_METHOD -> MethodFragment()
            TAG_SETTING_EXPENDITURE -> ExpenditureCategoryFragment()
            TAG_SETTING_EXPENDITURE_EDIT -> EditExpenditureCategoryFragment()
            TAG_SETTING_INCOME -> IncomeCategoryFragment()
            else -> {error("invalid tag")}
        }
    }
}
package com.nimok97.accountbook.presentation

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.nimok97.accountbook.R
import com.nimok97.accountbook.common.printLog
import com.nimok97.accountbook.databinding.ActivityMainBinding
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
import com.nimok97.accountbook.presentation.util.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val mainViewModel: MainViewModel by viewModels()

    private var FINISH_INTERVAL_TIME: Long = 2000
    private var backPressedTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        initView()
        collectData()
    }

    private fun initView() {
        setBottomNavigation()
        setTodayDate()
        setCurrentDate()
        changeTab(0, TAG_HISTORY, R.id.fragment_history)
    }

    private fun setTodayDate() {
        mainViewModel.todayYear = calculateCurrentYear()
        mainViewModel.todayMonth = calculateCurrentMonth()
        mainViewModel.todayDay = calculateCurrentDay()
    }

    private fun setCurrentDate() {
        mainViewModel.currentYear = calculateCurrentYear()
        mainViewModel.currentMonth = calculateCurrentMonth()
        mainViewModel.currentDay = calculateCurrentDay()
    }

    private fun setBottomNavigation() {
        binding.bnvMain.itemIconTintList = null

        binding.bnvMain.apply {
            setOnItemSelectedListener {
                when (it.itemId) {
                    R.id.fragment_history -> {
                        if (this.selectedItemId != R.id.fragment_history)
                            changeTab(0, TAG_HISTORY, R.id.fragment_history)
                    }
                    R.id.fragment_calendar -> {
                        if (this.selectedItemId != R.id.fragment_calendar) {
                            val calendarFragment = CalendarFragment()
                            changeTab(1, TAG_CALENDAR, R.id.fragment_calendar)
                        }
                    }
                    R.id.fragment_statistics -> {
                        if (this.selectedItemId != R.id.fragment_statistics)
                            changeTab(2, TAG_STATISTICS, R.id.fragment_statistics)
                    }
                    R.id.fragment_setting -> {
                        if (this.selectedItemId != R.id.fragment_setting)
                            changeTab(3, TAG_SETTING, R.id.fragment_setting)
                    }
                }
                true
            }
        }
    }

    private fun changeTab(tabIndex: Int, tag: String, id: Int) {
        FragmentStackManager.clearAllStack()
        FragmentStackManager.pushStack(tabIndex, tag)
        changeFragment(tag)
        binding.bnvMain.menu.getItem(tabIndex).isChecked = true
        printLog("change tab to : $tabIndex inx")
    }

    private fun changeFragment(tag: String) {
        supportFragmentManager.beginTransaction().replace(
            R.id.fcv_main,
            FragmentStackManager.returnFragmentByTag(tag)
        )
            .commit()
    }

    private fun collectData() {
        collectFabCliked()
        collectHistoryFragmentEvent()
        collectSettingFragmentEvent()
        collectBackEventInMethodFragment()
        collectBackButtonInAppBarPressed()
    }

    private fun collectFabCliked() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                mainViewModel.fabClickedEvent.collect {
                    if (it) {
                        FragmentStackManager.pushStack(0, TAG_HISTORY_ADD)
                        changeFragment(TAG_HISTORY_ADD)
                    }
                }
            }
        }
    }

    private fun collectHistoryFragmentEvent() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                mainViewModel.moveToEditHistoryFragmentEvent.collect {
                    if (it) {
                        FragmentStackManager.pushStack(0, TAG_HISTORY_EDIT)
                        changeFragment(TAG_HISTORY_EDIT)
                    }
                }
            }
        }
    }

    private fun collectSettingFragmentEvent() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                mainViewModel.moveToMethodFragmentEvent.collect {
                    if (it) {
                        when (binding.bnvMain.selectedItemId) {
                            R.id.fragment_history -> {
                                FragmentStackManager.pushStack(0, TAG_SETTING_METHOD)
                                changeFragment(TAG_SETTING_METHOD)
                            }
                            else -> {
                                FragmentStackManager.pushStack(3, TAG_SETTING_METHOD)
                                changeFragment(TAG_SETTING_METHOD)
                            }
                        }
                    }
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                mainViewModel.moveToExpenditureCategoryFragmentEvent.collect {
                    if (it) {
                        when (binding.bnvMain.selectedItemId) {
                            R.id.fragment_history -> {
                                FragmentStackManager.pushStack(0, TAG_SETTING_EXPENDITURE)
                                changeFragment(TAG_SETTING_EXPENDITURE)
                            }
                            else -> {
                                FragmentStackManager.pushStack(3, TAG_SETTING_EXPENDITURE)
                                changeFragment(TAG_SETTING_EXPENDITURE)
                            }
                        }
                    }
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                mainViewModel.moveToEditExpenditureCategoryFragmentEvent.collect {
                    if (it) {
                        FragmentStackManager.pushStack(3, TAG_SETTING_EXPENDITURE_EDIT)
                        changeFragment(TAG_SETTING_EXPENDITURE_EDIT)
                    }
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                mainViewModel.moveToIncomeCategoryFragmentEvent.collect {
                    if (it) {
                        when (binding.bnvMain.selectedItemId) {
                            R.id.fragment_history -> {
                                FragmentStackManager.pushStack(0, TAG_SETTING_INCOME)
                                changeFragment(TAG_SETTING_INCOME)
                            }
                            else -> {
                                FragmentStackManager.pushStack(3, TAG_SETTING_INCOME)
                                changeFragment(TAG_SETTING_INCOME)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun collectBackEventInMethodFragment() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                mainViewModel.backEventInMethodFragment.collect {
                    if (it) {
                        printLog("onback")
                        onBackPressed()
                    }
                }
            }
        }
    }

    private fun collectBackButtonInAppBarPressed() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                mainViewModel.backButtonPressedEvent.collect {
                    if (it) {
                        printLog("onback")
                        onBackPressed()
                    }
                }
            }
        }
    }

    override fun onBackPressed() {
        when (binding.bnvMain.selectedItemId) {
            R.id.fragment_history -> {
                if (FragmentStackManager.getStackSize(0) >= 2) {
                    FragmentStackManager.popStack(0)
                    changeFragment(FragmentStackManager.getTopStack(0))
                } else {
                    var tempTime = System.currentTimeMillis();
                    var intervalTime = tempTime - backPressedTime;
                    if (0 <= intervalTime && FINISH_INTERVAL_TIME >= intervalTime) {
                        super.onBackPressed();
                    } else {
                        backPressedTime = tempTime;
                        Toast.makeText(this, "'뒤로' 버튼을 한 번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT)
                            .show();
                        return
                    }
                }
            }
            R.id.fragment_calendar -> {
                if (FragmentStackManager.getStackSize(1) >= 2) {
                    FragmentStackManager.popStack(1)
                    changeFragment(FragmentStackManager.getTopStack(1))
                } else {
                    changeTab(0, TAG_HISTORY, R.id.fragment_history)
                }
            }
            R.id.fragment_statistics -> {
                if (FragmentStackManager.getStackSize(2) >= 2) {
                    FragmentStackManager.popStack(2)
                    changeFragment(FragmentStackManager.getTopStack(2))
                } else {
                    changeTab(0, TAG_HISTORY, R.id.fragment_history)
                }
            }
            else -> {
                if (FragmentStackManager.getStackSize(3) >= 2) {
                    FragmentStackManager.popStack(3)
                    changeFragment(FragmentStackManager.getTopStack(3))
                } else {
                    changeTab(0, TAG_HISTORY, R.id.fragment_history)
                }
            }
        }
    }
}
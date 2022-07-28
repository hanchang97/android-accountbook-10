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
import com.nimok97.accountbook.presentation.history.manage.ManageHistoryFragment
import com.nimok97.accountbook.presentation.setting.SettingFragment
import com.nimok97.accountbook.presentation.statistics.StatisticsFragment
import com.nimok97.accountbook.presentation.util.FragmentStackManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val mainViewModel: MainViewModel by viewModels()

    private var FINISH_INTERVAL_TIME: Long = 2000
    private var backPressedTime: Long = 0

    private val historyFragment by lazy { HistoryFragment() }
    private val calendarFragment by lazy { CalendarFragment() }
    private val statisticsFragment by lazy { StatisticsFragment() }
    private val settingFragment by lazy { SettingFragment() }

    private val manageHistoryFragment by lazy { ManageHistoryFragment() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        initView()
        collectData()
    }

    private fun initView() {
        setBottomNavigation()
        changeTab(0, historyFragment, R.id.fragment_history)
    }

    private fun setBottomNavigation() {
        binding.bnvMain.itemIconTintList = null

        binding.bnvMain.apply {
            setOnItemSelectedListener {
                when (it.itemId) {
                    R.id.fragment_history -> {
                        if (this.selectedItemId != R.id.fragment_history)
                            changeTab(0, historyFragment, R.id.fragment_history)
                    }
                    R.id.fragment_calendar -> {
                        if (this.selectedItemId != R.id.fragment_calendar)
                            changeTab(1, calendarFragment, R.id.fragment_calendar)
                    }
                    R.id.fragment_statistics -> {
                        if (this.selectedItemId != R.id.fragment_statistics)
                            changeTab(2, statisticsFragment, R.id.fragment_statistics)
                    }
                    R.id.fragment_setting -> {
                        if (this.selectedItemId != R.id.fragment_setting)
                            changeTab(3, settingFragment, R.id.fragment_setting)
                    }
                }
                true
            }
        }
    }

    private fun changeTab(tabIndex: Int, fragment: Fragment, id: Int) {
        FragmentStackManager.clearAllStack()
        FragmentStackManager.pushStack(tabIndex, fragment)
        changeFragment(fragment)
        binding.bnvMain.menu.getItem(tabIndex).isChecked = true
        printLog("change tab to : $tabIndex inx")
    }

    private fun changeFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.fcv_main, fragment)
            .commit()
    }

    private fun collectData() {
        collectFabCliked()
    }

    private fun collectFabCliked() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                mainViewModel.fabClickedEvent.collect {
                    if (it) {
                        FragmentStackManager.pushStack(0, manageHistoryFragment)
                        changeFragment(manageHistoryFragment)
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
                    changeTab(0, historyFragment, R.id.fragment_history)
                }
            }
            R.id.fragment_statistics -> {
                if (FragmentStackManager.getStackSize(2) >= 2) {
                    FragmentStackManager.popStack(2)
                    changeFragment(FragmentStackManager.getTopStack(2))
                } else {
                    changeTab(0, historyFragment, R.id.fragment_history)
                }
            }
            else -> {
                if (FragmentStackManager.getStackSize(3) >= 2) {
                    FragmentStackManager.popStack(3)
                    changeFragment(FragmentStackManager.getTopStack(3))
                } else {
                    changeTab(0, historyFragment, R.id.fragment_history)
                }
            }
        }
    }
}
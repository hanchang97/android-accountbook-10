package com.nimok97.accountbook.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.nimok97.accountbook.R
import com.nimok97.accountbook.databinding.ActivityMainBinding
import com.nimok97.accountbook.presentation.calendar.CalendarFragment
import com.nimok97.accountbook.presentation.history.HistoryFragment
import com.nimok97.accountbook.presentation.setting.SettingFragment
import com.nimok97.accountbook.presentation.statistics.StatisticsFragment
import com.nimok97.accountbook.presentation.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val historyFragment by lazy { HistoryFragment() }
    private val calendarFragment by lazy { CalendarFragment() }
    private val statisticsFragment by lazy { StatisticsFragment() }
    private val settingFragment by lazy { SettingFragment() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        initView()
    }

    private fun initView() {
        setBottomNavigation()
    }

    private fun setBottomNavigation() {
        binding.bnvMain.itemIconTintList = null

        binding.bnvMain.apply {
            setOnItemSelectedListener {
                when (it.itemId) {
                    R.id.fragment_history -> {
                        if(this.selectedItemId != R.id.fragment_history)
                            changeFragment(historyFragment, TAG_HISTORY)
                    }
                    R.id.fragment_calendar -> {
                        if(this.selectedItemId != R.id.fragment_calendar)
                            changeFragment(calendarFragment, TAG_CALENDAR)
                    }
                    R.id.fragment_statistics -> {
                        if(this.selectedItemId != R.id.fragment_statistics)
                            changeFragment(statisticsFragment, TAG_STATISTICS)
                    }
                    R.id.fragment_setting -> {
                        if(this.selectedItemId != R.id.fragment_setting)
                            changeFragment(settingFragment, TAG_SETTING)
                    }
                }
                true
            }
        }
    }

    private fun changeFragment(fragment: Fragment, tag: String) {
        supportFragmentManager.beginTransaction().replace(R.id.fcv_main, fragment, tag)
            .commit()
    }
}
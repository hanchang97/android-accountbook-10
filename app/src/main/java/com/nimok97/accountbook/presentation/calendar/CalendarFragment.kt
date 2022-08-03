package com.nimok97.accountbook.presentation.calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.nimok97.accountbook.R
import com.nimok97.accountbook.common.printLog
import com.nimok97.accountbook.databinding.FragmentCalendarBinding
import com.nimok97.accountbook.presentation.MainViewModel
import com.nimok97.accountbook.presentation.util.calculateCurrentMonthDayCount
import com.nimok97.accountbook.presentation.util.calculateCurrentMonthStartDay
import com.nimok97.accountbook.presentation.util.calculateFirstDayOfMonth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CalendarFragment : Fragment() {

    private lateinit var binding: FragmentCalendarBinding
    private val mainViewModel: MainViewModel by activityViewModels()
    private val calendarViewModel: CalendarViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        printLog("CalendarFragment / onCreateView")
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_calendar, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
    }

    private fun initView() {
        setCalendar()
    }

    private fun setCalendar() {
        calendarViewModel.currentYear = mainViewModel.currentYear
        calendarViewModel.currentMonth = mainViewModel.currentMonth

        calendarViewModel.dayCount = calculateCurrentMonthDayCount(
            calendarViewModel.currentYear,
            calendarViewModel.currentMonth
        )
        calendarViewModel.startDay = calculateCurrentMonthStartDay(
            calendarViewModel.currentYear,
            calendarViewModel.currentMonth
        )

        // 이전 달 몇일까지 있는지 구하기 위함
        if (calendarViewModel.currentMonth == 1) {
            calendarViewModel.preMonthDayCount = calculateCurrentMonthDayCount(
                calendarViewModel.currentYear - 1,
                12
            )
        } else {
            calendarViewModel.preMonthDayCount = calculateCurrentMonthDayCount(
                calendarViewModel.currentYear,
                calendarViewModel.currentMonth - 1
            )
        }

        calendarViewModel.getHistoryitemList()
    }
}
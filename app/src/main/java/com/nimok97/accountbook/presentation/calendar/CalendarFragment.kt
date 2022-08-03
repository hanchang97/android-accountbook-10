package com.nimok97.accountbook.presentation.calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import com.nimok97.accountbook.R
import com.nimok97.accountbook.common.getCurrentHistoryDateString
import com.nimok97.accountbook.common.printLog
import com.nimok97.accountbook.databinding.FragmentCalendarBinding
import com.nimok97.accountbook.presentation.MainViewModel
import com.nimok97.accountbook.presentation.calendar.adpater.CalendarDataItemAdapter
import com.nimok97.accountbook.presentation.util.CustomAppBar
import com.nimok97.accountbook.presentation.util.calculateCurrentMonthDayCount
import com.nimok97.accountbook.presentation.util.calculateCurrentMonthStartDay
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CalendarFragment : Fragment() {

    private lateinit var binding: FragmentCalendarBinding
    private val mainViewModel: MainViewModel by activityViewModels()
    private val calendarViewModel: CalendarViewModel by viewModels()
    private lateinit var calendarDataItemAdapter: CalendarDataItemAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        printLog("CalendarFragment / onCreateView")
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_calendar, container, false)
        binding.calendarViewModel = calendarViewModel
        binding.lifecycleOwner = this.viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
    }

    private fun initView() {
        setAppBar()
        setCalendar()
        setRecyclerView()
        collectData()
    }

    private fun setAppBar() {
        binding.customAppBar.setOnLeftImageClickListener(LeftListener())
        binding.customAppBar.setOnRightImageClickListener(RightListener())
    }

    private fun setCalendar() {
        calendarViewModel.currentYear = mainViewModel.currentYear
        calendarViewModel.currentMonth = mainViewModel.currentMonth

        binding.customAppBar.setTitle(
            getCurrentHistoryDateString(
                calendarViewModel.currentYear,
                calendarViewModel.currentMonth
            )
        )

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

    private fun setRecyclerView() {
        calendarDataItemAdapter = CalendarDataItemAdapter()
        binding.rvCalendar.apply {
            adapter = calendarDataItemAdapter
            layoutManager = GridLayoutManager(requireContext(), 7)
            itemAnimator = null
        }
    }

    private fun collectData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                calendarViewModel.calendarDataListFlow.collect {
                    calendarDataItemAdapter.submitList(it.toList())
                }
            }
        }
    }

    inner class LeftListener : CustomAppBar.LeftImageClickListener {
        override fun clickLeft(view: View) {
            if (mainViewModel.currentMonth > 1) {
                mainViewModel.currentMonth -= 1
            } else {
                mainViewModel.currentMonth = 12
                mainViewModel.currentYear -= 1
            }
            setCalendar()
        }
    }

    inner class RightListener : CustomAppBar.RightImageClickListener {
        override fun clickRight(view: View) {
            if (mainViewModel.currentMonth < 12) {
                mainViewModel.currentMonth += 1
            } else {
                mainViewModel.currentMonth = 1
                mainViewModel.currentYear += 1
            }
            setCalendar()
        }
    }
}
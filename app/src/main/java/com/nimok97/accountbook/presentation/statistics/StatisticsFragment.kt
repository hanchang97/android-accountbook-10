package com.nimok97.accountbook.presentation.statistics

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.nimok97.accountbook.R
import com.nimok97.accountbook.common.getCurrentHistoryDateString
import com.nimok97.accountbook.common.printLog
import com.nimok97.accountbook.databinding.FragmentStatisticsBinding
import com.nimok97.accountbook.presentation.MainViewModel
import com.nimok97.accountbook.presentation.util.CustomAppBar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StatisticsFragment : Fragment() {

    private lateinit var binding: FragmentStatisticsBinding
    private val mainViewModel: MainViewModel by activityViewModels()
    private val statisticsViewModel: StatisticsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        printLog("${this.javaClass.simpleName} / onCreateView")
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_statistics, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
    }

    private fun initView() {
        setAppBarTitle()
        setAppBarListener()
        setData()
    }

    private fun setAppBarTitle() {
        binding.customAppBar.setTitle(
            getCurrentHistoryDateString(
                mainViewModel.currentYear,
                mainViewModel.currentMonth
            )
        )
    }

    private fun setAppBarListener() {
        binding.customAppBar.setOnLeftImageClickListener(LeftListener())
        binding.customAppBar.setOnRightImageClickListener(RightListener())
    }

    private fun setData() {
        statisticsViewModel.setYearAndMonth(mainViewModel.currentYear, mainViewModel.currentMonth)
    }

    inner class LeftListener : CustomAppBar.LeftImageClickListener {
        override fun clickLeft(view: View) {
            if (mainViewModel.currentMonth > 1) {
                mainViewModel.currentMonth -= 1
            } else {
                mainViewModel.currentMonth = 12
                mainViewModel.currentYear -= 1
            }
            setAppBarTitle()
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
            setAppBarTitle()
        }
    }
}
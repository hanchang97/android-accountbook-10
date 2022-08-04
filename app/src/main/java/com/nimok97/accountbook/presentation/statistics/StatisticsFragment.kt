package com.nimok97.accountbook.presentation.statistics

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.nimok97.accountbook.R
import com.nimok97.accountbook.common.getCurrentHistoryDateString
import com.nimok97.accountbook.common.printLog
import com.nimok97.accountbook.databinding.FragmentStatisticsBinding
import com.nimok97.accountbook.presentation.MainViewModel
import com.nimok97.accountbook.presentation.util.CustomAppBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

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
        binding.statisticsViewModel = statisticsViewModel
        binding.lifecycleOwner = this.viewLifecycleOwner
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
        setPieChartOption()
        setPieChartData()
        collectData()
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
        statisticsViewModel.getHistoryitemList()
    }

    private fun setPieChartOption() {
        with(binding.pieChart) {
            isDrawHoleEnabled = true
            setHoleColor(ContextCompat.getColor(requireContext(), R.color.primary_off_white))
            setEntryLabelTextSize(10f) // 항목 내용 텍스트 사이즈
            setEntryLabelTypeface(Typeface.DEFAULT_BOLD)
            legend.isEnabled = false
            description.isEnabled = false
        }
    }

    private fun setPieChartData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                statisticsViewModel.categoryStatisticsListFlow.collect {
                    val entries = ArrayList<PieEntry>()
                    val colors = ArrayList<Int>()
                    it.forEach {
                        entries.add(PieEntry(it.percent, it.content))
                        colors.add(android.graphics.Color.parseColor(it.color))
                    }

                    val dataSet = PieDataSet(entries, "Expenditure Statistics")
                    dataSet.setColors(colors)

                    val data = PieData(dataSet)
                    data.setDrawValues(true)
                    data.setValueTextSize(0f) // 차트에 항목 별 퍼센트 수치 표시 제거 위함

                    binding.pieChart.data = data
                    binding.pieChart.animateXY(1000, 1000)
                    binding.pieChart.invalidate()
                }
            }
        }
    }

    private fun collectData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                statisticsViewModel.emptyEvent.collect {
                    if(it) {
                        // 지출 내역 없습니다 문구 나타내기

                    }
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
            setAppBarTitle()
            setData()
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
            setData()
        }
    }
}
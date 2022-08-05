package com.nimok97.accountbook.presentation.statistics

import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import com.nimok97.accountbook.domain.model.CategoryStatistics
import com.nimok97.accountbook.presentation.MainViewModel
import com.nimok97.accountbook.presentation.theme.*
import com.nimok97.accountbook.presentation.util.CustomAppBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.DecimalFormat
import kotlin.math.floor

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
        setComposeView()
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
                    if (it) {
                        // 지출 내역 없습니다 문구 나타내기

                    }
                }
            }
        }
    }

    private fun setComposeView() {
        binding.composeView.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                Surface(
                    color = Primary_off_white,
                    modifier = Modifier.fillMaxSize()
                ) {
                    CategoryList()
                }
            }
        }
    }

    @Composable
    fun CategoryList() {
        val categoryListState =
            statisticsViewModel.categoryStatisticsListFlow.collectAsState().value
        LazyColumn() {
            items(categoryListState) { categoryStatistics ->
                CategoryItem(categoryStatistics)
            }
        }
    }

    @Composable
    fun CategoryItem(categoryStatistics: CategoryStatistics) {
        val intColor = android.graphics.Color.parseColor(categoryStatistics.color)
        val decimalFormat = DecimalFormat("#,###")

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp, 0.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Card(
                    shape = RoundedCornerShape(50),
                    backgroundColor = Color(intColor),
                    elevation = 0.dp
                ) {
                    Text(
                        text = categoryStatistics.content,
                        modifier = Modifier
                            .defaultMinSize(56.dp, 0.dp)
                            .padding(4.dp),
                        style = TextStyle(
                            color = White,
                            fontFamily = kopubworld_dotum_pro,
                            fontWeight = FontWeight.Bold
                        ),
                        fontSize = 10.sp,
                        textAlign = TextAlign.Center
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    modifier = Modifier.weight(1f),
                    text = decimalFormat.format(categoryStatistics.amount),
                    style = TextStyle(
                        color = Primary_purple,
                        fontFamily = kopubworld_dotum_pro,
                        fontWeight = FontWeight.Medium,
                    ),
                    fontSize = 14.sp
                )
                Text(
                    text = "${(floor(categoryStatistics.percent * 100)).toInt()}%",
                    style = TextStyle(
                        color = Primary_purple,
                        fontFamily = kopubworld_dotum_pro,
                        fontWeight = FontWeight.Bold,
                    ),
                    fontSize = 14.sp
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp),
                color = Primary_purple_40
            )
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
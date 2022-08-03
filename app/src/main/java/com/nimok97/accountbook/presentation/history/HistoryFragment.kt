package com.nimok97.accountbook.presentation.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.nimok97.accountbook.R
import com.nimok97.accountbook.common.getCurrentHistoryDateString
import com.nimok97.accountbook.common.printLog
import com.nimok97.accountbook.data.dao.HistoryDao
import com.nimok97.accountbook.databinding.FragmentHistoryBinding
import com.nimok97.accountbook.presentation.MainViewModel
import com.nimok97.accountbook.presentation.util.CustomAppBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HistoryFragment : Fragment() {

    private lateinit var binding: FragmentHistoryBinding
    private val mainViewModel: MainViewModel by activityViewModels()
    private val historyViewModel: HistoryViewModel by activityViewModels()

    private lateinit var historyItemAdapter: HistoryItemAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        printLog("${this.javaClass.simpleName} / onCreateView")
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_history, container, false)
        binding.mainViewModel = mainViewModel
        binding.historyViewModel = historyViewModel
        binding.lifecycleOwner = this.viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //saveDefaultData()

        initView()
    }

    private fun saveDefaultData() {
        historyViewModel.addHistory(
            HistoryDao(
                1, 2022, 7, 29,
                "금", 10000, "도시락", 1, 10
            )
        )
        historyViewModel.addHistory(
            HistoryDao(
                1, 2022, 7, 28,
                "목", 40000, "청바지 구입", 2, 4
            )
        )
        historyViewModel.addHistory(
            HistoryDao(
                1, 2022, 7, 16,
                "토", 15000, "영화 관람", 2, 7
            )
        )
    }

    private fun initView() {
        setAppBar()
        setRecyclerView()
        collectData()

        historyViewModel.getHistoryItemList(2022, 7)
    }

    private fun setAppBar() {
        binding.customAppBar.setOnLeftImageClickListener(LeftListener())
        binding.customAppBar.setOnRightImageClickListener(RightListener())
        binding.customAppBar.setTitle("2022년 7월")
    }

    private fun setRecyclerView() {
        historyItemAdapter = HistoryItemAdapter({
            // 일반 모드에서 아이템 선택 -> 수정 화면
            historyViewModel.selectedHistoryForEdit = it
            mainViewModel.moveToEditHistoryFragment()
        }, { pos, id ->
            printLog("history item long clicked")
            historyItemAdapter.enableLongClickMode(pos)
            mainViewModel.setLongClickMode(true)
            historyViewModel.addOrRemoveDeleteId(id)
        }, {
            // 롱클릭 모드에서 아이템 클릭 시
                id ->
            if (historyViewModel.addOrRemoveDeleteId(id) > 0) {
                binding.customAppBar.setTitle("${historyViewModel.deleteIdSet.size}개 선택")
            } else { // 선택된 아이템 0개
                mainViewModel.setLongClickMode(false)
                historyItemAdapter.disableLongClickMode()
                historyViewModel.deleteIdSet.clear()
            }
        })
        binding.rvHistory.apply {
            adapter = historyItemAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun collectData() {
        collectLongClikModeData()

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                historyViewModel.historyItemListFlow.collect {
                    historyItemAdapter.submitList(it)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                historyViewModel.emptyEvent.collect {
                    if (it) {
                        binding.rvHistory.isVisible = false
                        binding.tvHistoryEmpty.isVisible = true
                    } else {
                        binding.rvHistory.isVisible = true
                        binding.tvHistoryEmpty.isVisible = false
                    }
                }
            }
        }
    }

    private fun collectLongClikModeData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                mainViewModel.isLongClickModeFlow.collect {
                    if (it) {
                        with(binding.customAppBar) {
                            setTitle("1개 선택")
                            setLeftImage(R.drawable.ic_back)
                            setRightImage(R.drawable.ic_trash)
                        }
                    } else {
                        with(binding.customAppBar) {
                            setTitle(
                                getCurrentHistoryDateString(
                                    mainViewModel.currentYear,
                                    mainViewModel.currentMonth
                                )
                            )
                            setLeftImage(R.drawable.ic_left)
                            setRightImage(R.drawable.ic_right)
                        }
                    }
                }
            }
        }
    }

    inner class LeftListener : CustomAppBar.LeftImageClickListener {
        override fun clickLeft(view: View) {
            if (mainViewModel.isLongClickModeFlow.value) {
                // 롱클릭 모드 해제하기
                mainViewModel.setLongClickMode(false)
                historyItemAdapter.disableLongClickMode()
                historyViewModel.deleteIdSet.clear()
            } else {
                printLog("Long click mode disabled : left clicked")
            }
        }
    }

    inner class RightListener : CustomAppBar.RightImageClickListener {
        override fun clickRight(view: View) {
            if (mainViewModel.isLongClickModeFlow.value) {
                // 선택된 아이템들 삭제 후 롱클릭 모드 해제하기
                historyViewModel.deleteHistories()
            } else {
                printLog("Long click mode disabled : right clicked")
            }
        }
    }
}
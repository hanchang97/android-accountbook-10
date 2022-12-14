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
import com.nimok97.accountbook.presentation.history.adpater.HistoryItemAdapter
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
                "???", 10000, "?????????", 1, 10
            )
        )
        historyViewModel.addHistory(
            HistoryDao(
                1, 2022, 7, 28,
                "???", 40000, "????????? ??????", 2, 4
            )
        )
        historyViewModel.addHistory(
            HistoryDao(
                1, 2022, 7, 16,
                "???", 15000, "?????? ??????", 2, 7
            )
        )
    }

    private fun initView() {
        setAppBar()
        setRecyclerView()
        collectData()

        historyViewModel.getHistoryItemList(mainViewModel.currentYear, mainViewModel.currentMonth)
    }

    private fun setAppBar() {
        binding.customAppBar.setOnLeftImageClickListener(LeftListener())
        binding.customAppBar.setOnRightImageClickListener(RightListener())
        binding.customAppBar.setTitle("2022??? 7???")
    }

    private fun setRecyclerView() {
        historyItemAdapter = HistoryItemAdapter({
            // ?????? ???????????? ????????? ?????? -> ?????? ??????
            historyViewModel.selectedHistoryForEdit = it
            mainViewModel.moveToEditHistoryFragment()
        }, { pos, id ->
            printLog("history item long clicked")
            historyItemAdapter.enableLongClickMode(pos)
            mainViewModel.setLongClickMode(true)
            historyViewModel.addOrRemoveDeleteId(id)
        }, {
            // ????????? ???????????? ????????? ?????? ???
                id ->
            if (historyViewModel.addOrRemoveDeleteId(id) > 0) {
                binding.customAppBar.setTitle("${historyViewModel.deleteIdSet.size}??? ??????")
            } else { // ????????? ????????? 0???
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
                            setTitle("1??? ??????")
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
                // ????????? ?????? ????????????
                mainViewModel.setLongClickMode(false)
                historyItemAdapter.disableLongClickMode()
                historyViewModel.deleteIdSet.clear()
            } else {
                printLog("Long click mode disabled : left clicked")
                if (mainViewModel.currentMonth > 1) {
                    mainViewModel.currentMonth -= 1
                } else {
                    mainViewModel.currentMonth = 12
                    mainViewModel.currentYear -= 1
                }
                historyViewModel.getHistoryItemList(
                    mainViewModel.currentYear,
                    mainViewModel.currentMonth
                )
                binding.customAppBar.setTitle(
                    getCurrentHistoryDateString(
                        mainViewModel.currentYear,
                        mainViewModel.currentMonth
                    )
                )
            }
        }
    }

    inner class RightListener : CustomAppBar.RightImageClickListener {
        override fun clickRight(view: View) {
            if (mainViewModel.isLongClickModeFlow.value) {
                // ????????? ???????????? ?????? ??? ????????? ?????? ????????????
                historyViewModel.deleteHistories(
                    mainViewModel.currentYear,
                    mainViewModel.currentMonth
                )
                mainViewModel.setLongClickMode(false)
            } else {
                printLog("Long click mode disabled : right clicked")
                if (mainViewModel.currentMonth < 12) {
                    mainViewModel.currentMonth += 1
                } else {
                    mainViewModel.currentMonth = 1
                    mainViewModel.currentYear += 1
                }
                historyViewModel.getHistoryItemList(
                    mainViewModel.currentYear,
                    mainViewModel.currentMonth
                )
                binding.customAppBar.setTitle(
                    getCurrentHistoryDateString(
                        mainViewModel.currentYear,
                        mainViewModel.currentMonth
                    )
                )
            }
        }
    }
}
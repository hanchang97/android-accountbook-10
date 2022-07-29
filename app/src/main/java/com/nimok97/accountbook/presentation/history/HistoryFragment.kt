package com.nimok97.accountbook.presentation.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.nimok97.accountbook.R
import com.nimok97.accountbook.common.printLog
import com.nimok97.accountbook.data.dao.HistoryDao
import com.nimok97.accountbook.databinding.FragmentHistoryBinding
import com.nimok97.accountbook.presentation.MainViewModel
import com.nimok97.accountbook.presentation.util.CustomAppBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
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
        printLog("HistoryFragment / onCreateView")
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_history, container, false)
        binding.mainViewModel = mainViewModel
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
                0, 2022, 7, 16,
                "토", 20000, "용돈", -1, 2
            )
        )
        historyViewModel.addHistory(
            HistoryDao(
                1, 2022, 7, 9,
                "토", 50000, "전시회 방문", 2, 7
            )
        )
        historyViewModel.addHistory(
            HistoryDao(
                0, 2022, 7, 3,
                "일", 5000, "당근마켓", -1, 8
            )
        )
    }

    private fun initView() {
        setFab()
        setAppBar()
        setRecyclerView()
        observeData()

        historyViewModel.getHistoryItemList(2022, 7)
    }

    private fun setFab() {

    }

    private fun setAppBar() {
        binding.customAppBar.setOnLeftImageClickListener(LeftListener())
        binding.customAppBar.setOnRightImageClickListener(RightListener())
        binding.customAppBar.setTitle("2022년 7월")
    }

    private fun setRecyclerView() {
        historyItemAdapter = HistoryItemAdapter()
        binding.rvHistory.apply {
            adapter = historyItemAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun observeData(){
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                historyViewModel.historyItemListFlow.collect{
                    historyItemAdapter.submitList(it)
                }
            }
        }
    }

    inner class LeftListener : CustomAppBar.LeftImageClickListener {
        override fun clickLeft(view: View) {
            printLog("left clicked")
        }
    }

    inner class RightListener : CustomAppBar.RightImageClickListener {
        override fun clickRight(view: View) {
            printLog("right clicked")
        }
    }
}
package com.nimok97.accountbook.presentation.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.nimok97.accountbook.R
import com.nimok97.accountbook.common.printLog
import com.nimok97.accountbook.data.dao.HistoryDao
import com.nimok97.accountbook.databinding.FragmentHistoryBinding
import com.nimok97.accountbook.presentation.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HistoryFragment : Fragment() {

    private lateinit var binding: FragmentHistoryBinding
    private val mainViewModel: MainViewModel by activityViewModels()
    private val historyViewModel: HistoryViewModel by activityViewModels()

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

    private fun saveDefaultData(){
        historyViewModel.addHistory(HistoryDao(0, 2022, 7, 9,
            "토", 10000, "용돈", -1, 2))
        historyViewModel.addHistory(HistoryDao(0, 2022, 7, 9,
            "토", 500000, "아르바이트 월급", -1, 1))
        historyViewModel.addHistory(HistoryDao(1, 2022, 7, 3,
            "일", 30000, "반팔티 구매", 2, 4))
        historyViewModel.addHistory(HistoryDao(1, 2022, 7, 28,
            "목", 20000, "저녁 식사", 1, 10))
        historyViewModel.addHistory(HistoryDao(1, 2022, 7, 29,
            "금", 40000, "치과 진료비", 1, 9))
    }

    private fun initView() {
        setFab()
        historyViewModel.getHistoryItemList(2022, 7)
    }

    private fun setFab() {

    }
}
package com.nimok97.accountbook.presentation.history.manage.add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.nimok97.accountbook.R
import com.nimok97.accountbook.common.printLog
import com.nimok97.accountbook.databinding.FragmentAddHistoryBinding
import com.nimok97.accountbook.domain.model.Method
import com.nimok97.accountbook.presentation.history.HistoryViewModel
import com.nimok97.accountbook.presentation.history.manage.adapter.CustomSpinnerAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddHistoryFragment : Fragment() {

    private lateinit var binding: FragmentAddHistoryBinding
    private val historyViewModel: HistoryViewModel by activityViewModels()
    private val addHistoryViewModel: AddHistoryViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        printLog("${this.javaClass.simpleName} / onCreateView")
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_add_history, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
    }

    private fun initView() {
        addHistoryViewModel.getAllMethod()
        addHistoryViewModel.getAllCategory()

        setSpinner()
    }

    private fun setSpinner() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                addHistoryViewModel.methodistFlow.collect {
                    val methodSpinnerList = it.toMutableList()
                    methodSpinnerList.apply {
                        add(Method(-1, "추가하기"))
                        add(Method(-1, "선택하세요"))
                    }

                    val adapter = CustomSpinnerAdapter(requireContext(), methodSpinnerList)
                    with(binding) {
                        spinnerMethod.adapter = adapter
                        spinnerMethod.setSelection(methodSpinnerList.lastIndex)
                        spinnerMethod.onItemSelectedListener =
                            object : AdapterView.OnItemSelectedListener {
                                override fun onItemSelected(
                                    adapterView: AdapterView<*>?,
                                    view: View?,
                                    position: Int,
                                    id: Long
                                ) {
                                    if (position == methodSpinnerList.lastIndex - 1) {
                                        // 추가하기
                                    } else {
                                        adapterView?.findViewById<TextView>(R.id.tv_content)?.text =
                                            methodSpinnerList[position].content
                                        addHistoryViewModel.selectedMethodId =
                                            methodSpinnerList[position].id
                                    }
                                    // 뷰모델에서 버튼 활성화 여부 체크 하기
                                }

                                override fun onNothingSelected(adapterView: AdapterView<*>?) {

                                }
                            }
                    }
                }
            }
        }
    }

}

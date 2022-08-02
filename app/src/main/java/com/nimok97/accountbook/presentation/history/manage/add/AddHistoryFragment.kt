package com.nimok97.accountbook.presentation.history.manage.add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.TextView
import androidx.core.view.isVisible
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
import com.nimok97.accountbook.domain.model.Category
import com.nimok97.accountbook.domain.model.Method
import com.nimok97.accountbook.presentation.history.HistoryViewModel
import com.nimok97.accountbook.presentation.history.manage.adapter.CustomCategorySpinnerAdapter
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
        binding.addHistoryViewModel = addHistoryViewModel
        binding.lifecycleOwner = this.viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        collectData()
    }

    private fun initView() {
        addHistoryViewModel.getAllMethod()
        addHistoryViewModel.getAllCategory()

        setMethodSpinner()
    }

    private fun setMethodSpinner() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                addHistoryViewModel.methodistFlow.collect {
                    val methodSpinnerList = it.toMutableList()
                    methodSpinnerList.apply {
                        add(Method(-1, "추가하기"))
                        add(Method(-1, "선택하세요"))
                    }

                    val adapter = CustomSpinnerAdapter(requireContext(), methodSpinnerList)

                    binding.spinnerMethod.adapter = adapter
                    binding.spinnerMethod.setSelection(methodSpinnerList.lastIndex)
                    binding.spinnerMethod.onItemSelectedListener =
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

    private fun setIncomeCategorySpinner() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                addHistoryViewModel.categoryIncomeListFlow.collect {
                    val categorySpinnerList = it.toMutableList()
                    categorySpinnerList.apply {
                        add(Category(-1, 0, "추가하기", "#FFFFFF"))
                        add(Category(-1, 0, "선택하세요", "#FFFFFF"))
                    }

                    val adapter =
                        CustomCategorySpinnerAdapter(requireContext(), categorySpinnerList)

                    binding.spinnerCategory.adapter = adapter
                    binding.spinnerCategory.setSelection(categorySpinnerList.lastIndex)
                    binding.spinnerCategory.onItemSelectedListener =
                        object : AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(
                                adapterView: AdapterView<*>?,
                                view: View?,
                                position: Int,
                                id: Long
                            ) {
                                if (position == categorySpinnerList.lastIndex - 1) {
                                    // 추가하기 기능
                                } else {
                                    adapterView?.findViewById<TextView>(R.id.tv_content)?.text =
                                        categorySpinnerList[position].content
                                    addHistoryViewModel.selectedCategoryId =
                                        categorySpinnerList[position].id
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

    private fun setExpenditureCategorySpinner() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                addHistoryViewModel.categoryExpenditureListFlow.collect {
                    val categorySpinnerList = it.toMutableList()
                    categorySpinnerList.apply {
                        add(Category(-1, 0, "추가하기", "#FFFFFF"))
                        add(Category(-1, 0, "선택하세요", "#FFFFFF"))
                    }

                    val adapter =
                        CustomCategorySpinnerAdapter(requireContext(), categorySpinnerList)

                    binding.spinnerCategory.adapter = adapter
                    binding.spinnerCategory.setSelection(categorySpinnerList.lastIndex)
                    binding.spinnerCategory.onItemSelectedListener =
                        object : AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(
                                adapterView: AdapterView<*>?,
                                view: View?,
                                position: Int,
                                id: Long
                            ) {
                                if (position == categorySpinnerList.lastIndex - 1) {
                                    // 추가하기 기능
                                } else {
                                    adapterView?.findViewById<TextView>(R.id.tv_content)?.text =
                                        categorySpinnerList[position].content
                                    addHistoryViewModel.selectedCategoryId =
                                        categorySpinnerList[position].id
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

    private fun collectData() {
        collectSelectedType()
    }

    private fun collectSelectedType() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                addHistoryViewModel.incomeCheckedFlow.collect {
                    binding.tvMethodDescription.isVisible = !it
                    binding.spinnerMethod.isVisible = !it
                    binding.viewDividerUnderMethodSpinner.isVisible = !it

                    if (it) setIncomeCategorySpinner()
                    else setExpenditureCategorySpinner()
                }
            }
        }
    }
}

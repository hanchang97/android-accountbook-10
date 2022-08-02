package com.nimok97.accountbook.presentation.history.manage.add

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.datepicker.MaterialDatePicker
import com.nimok97.accountbook.R
import com.nimok97.accountbook.common.printLog
import com.nimok97.accountbook.databinding.FragmentAddHistoryBinding
import com.nimok97.accountbook.domain.model.Category
import com.nimok97.accountbook.domain.model.Method
import com.nimok97.accountbook.presentation.history.HistoryViewModel
import com.nimok97.accountbook.presentation.history.manage.adapter.CustomCategorySpinnerAdapter
import com.nimok97.accountbook.presentation.history.manage.adapter.CustomSpinnerAdapter
import com.nimok97.accountbook.presentation.util.calculateDayString
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*


@AndroidEntryPoint
class AddHistoryFragment : Fragment() {

    private lateinit var binding: FragmentAddHistoryBinding
    private val historyViewModel: HistoryViewModel by activityViewModels()
    private val addHistoryViewModel: AddHistoryViewModel by viewModels()

    val datePicker =
        MaterialDatePicker.Builder.datePicker()
            .setTitleText("날짜를 선택하세요")
            .build()

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

        setDateListener()
        setMethodSpinner()
        setAmountListener()
        setContentListener()
    }

    private fun setDateListener() {
        datePicker.addOnPositiveButtonClickListener {
            val dateStr = convertLongToDate(it)
            val dateStrList = dateStr.split('-')

            addHistoryViewModel.year = dateStrList[0].toInt()
            addHistoryViewModel.month = dateStrList[1].toInt()
            addHistoryViewModel.dayNum = dateStrList[2].toInt()
            addHistoryViewModel.dayStr = calculateDayString(
                addHistoryViewModel.year,
                addHistoryViewModel.month,
                addHistoryViewModel.dayNum
            )
            addHistoryViewModel.setDateString()
            addHistoryViewModel.checkData()
        }
    }

    private fun convertLongToDate(time: Long): String {
        val date = Date(time)
        val format = SimpleDateFormat("yyyy-MM-dd", Locale.KOREA)

        return format.format(date)
    }

    private fun setAmountListener() {
        binding.etAmount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString().isNotEmpty()) {
                    val amount = s.toString().toInt()
                    addHistoryViewModel.amount = amount
                    addHistoryViewModel.checkData()
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun setContentListener() {
        binding.etContent.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                addHistoryViewModel.content = s.toString()
                addHistoryViewModel.checkData()
            }

            override fun afterTextChanged(s: Editable?) {}
        })
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
                                    addHistoryViewModel.checkData()
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
                                    addHistoryViewModel.checkData()
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
        collectDateCliked()
        collectButtonActivate()
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

    private fun collectDateCliked() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                addHistoryViewModel.dateClickedEvent.collect {
                    if (it) {
                        datePicker.show(childFragmentManager, "date")
                    }
                }
            }
        }
    }

    private fun collectButtonActivate() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                addHistoryViewModel.buttonActiveFlow.collect {
                    changeButtonState(it)
                }
            }
        }
    }

    private fun changeButtonState(enable: Boolean) {
        if (enable) {
            binding.btnAdd.isEnabled = true
            binding.btnAdd.backgroundTintList =
                ContextCompat.getColorStateList(requireContext(), R.color.primary_yellow)
        } else {
            binding.btnAdd.isEnabled = false
            binding.btnAdd.backgroundTintList =
                ContextCompat.getColorStateList(requireContext(), R.color.primary_yellow_50)
        }
    }

}

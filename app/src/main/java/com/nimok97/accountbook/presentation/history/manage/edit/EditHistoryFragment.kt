package com.nimok97.accountbook.presentation.history.manage.edit

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.TextView
import android.widget.Toast
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
import com.nimok97.accountbook.databinding.FragmentEditHistoryBinding
import com.nimok97.accountbook.domain.model.Category
import com.nimok97.accountbook.domain.model.Method
import com.nimok97.accountbook.presentation.MainViewModel
import com.nimok97.accountbook.presentation.history.HistoryViewModel
import com.nimok97.accountbook.presentation.history.manage.adapter.CustomCategorySpinnerAdapter
import com.nimok97.accountbook.presentation.history.manage.adapter.CustomSpinnerAdapter
import com.nimok97.accountbook.presentation.util.CustomAppBar
import com.nimok97.accountbook.presentation.util.calculateDayString
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class EditHistoryFragment : Fragment() {

    private lateinit var binding: FragmentEditHistoryBinding
    private val editHistoryViewModel: EditHistoryViewModel by viewModels()
    private val historyViewModel: HistoryViewModel by activityViewModels()
    private val mainViewModel: MainViewModel by activityViewModels()

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
            DataBindingUtil.inflate(inflater, R.layout.fragment_edit_history, container, false)
        binding.editHistoryViewModel = editHistoryViewModel
        binding.historyViewModel = historyViewModel
        binding.lifecycleOwner = this.viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        collectData()
    }

    private fun initView() {
        editHistoryViewModel.setOrginData(historyViewModel.selectedHistoryForEdit)
        setMethodVisibility()
        setAppBar()
        setFilterBar()
        setDateListener()
        loadData()
        setAmountListener()
        setContentListener()
    }

    private fun setMethodVisibility() {
        binding.tvMethodDescription.isVisible = (editHistoryViewModel.categoryType == 1)
        binding.spinnerMethod.isVisible = (editHistoryViewModel.categoryType == 1)
        binding.viewDividerUnderMethodSpinner.isVisible = (editHistoryViewModel.categoryType == 1)
    }

    private fun setAppBar() {
        binding.customAppBar.setOnLeftImageClickListener(LeftListener())
    }

    private fun setFilterBar() {
        when (editHistoryViewModel.categoryType) {
            0 -> {
                binding.clIncome.setBackgroundColor(R.color.primary_purple)
                binding.clExpenditure.setBackgroundColor(R.color.primary_light_purple)
            }
            else -> {
                binding.clIncome.setBackgroundColor(R.color.primary_light_purple)
                binding.clExpenditure.setBackgroundColor(R.color.primary_purple)
            }
        }
    }

    private fun setDateListener() {
        datePicker.addOnPositiveButtonClickListener {
            val dateStr = convertLongToDate(it)
            val dateStrList = dateStr.split('-')

            editHistoryViewModel.year = dateStrList[0].toInt()
            editHistoryViewModel.month = dateStrList[1].toInt()
            editHistoryViewModel.dayNum = dateStrList[2].toInt()
            editHistoryViewModel.dayStr = calculateDayString(
                editHistoryViewModel.year,
                editHistoryViewModel.month,
                editHistoryViewModel.dayNum
            )
            editHistoryViewModel.setDateString()
            editHistoryViewModel.checkData()
        }
    }

    private fun convertLongToDate(time: Long): String {
        val date = Date(time)
        val format = SimpleDateFormat("yyyy-MM-dd", Locale.KOREA)

        return format.format(date)
    }

    private fun loadData() {
        editHistoryViewModel.getAllMethod()
        editHistoryViewModel.getAllCategory()
    }

    private fun setAmountListener() {
        binding.etAmount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString().isNotEmpty()) {
                    val amount = s.toString().toInt()
                    editHistoryViewModel.amount = amount
                    editHistoryViewModel.checkData()
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun setContentListener() {
        binding.etContent.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                editHistoryViewModel.content = s.toString()
                editHistoryViewModel.checkData()
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun collectData() {
        collectDateCliked()
        setMethodSpinner()
        setCategorySpinner()
        collectButtonActivate()
        collectUpdateHistorySuccess()
    }

    private fun collectDateCliked() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                editHistoryViewModel.dateClickedEvent.collect {
                    if (it) {
                        datePicker.show(childFragmentManager, "date")
                    }
                }
            }
        }
    }

    private fun setMethodSpinner() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                editHistoryViewModel.methodistFlow.collect {
                    val methodSpinnerList = it.toMutableList()
                    methodSpinnerList.apply {
                        add(Method(-1, "추가하기"))
                        add(Method(-1, "선택하세요"))
                    }

                    val adapter = CustomSpinnerAdapter(requireContext(), methodSpinnerList)

                    binding.spinnerMethod.adapter = adapter
                    var inx = 0
                    (0 until methodSpinnerList.size).forEach {
                        if (editHistoryViewModel.selectedMethodId == methodSpinnerList[it].id) inx =
                            it
                    }
                    binding.spinnerMethod.setSelection(inx)
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
                                    mainViewModel.moveToMethodFragment()
                                } else {
                                    adapterView?.findViewById<TextView>(R.id.tv_content)?.text =
                                        methodSpinnerList[position].content
                                    editHistoryViewModel.selectedMethodId =
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

    private fun setCategorySpinner() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                editHistoryViewModel.categoryListFlow.collect {
                    val categorySpinnerList = it.toMutableList()
                    categorySpinnerList.apply {
                        add(Category(-1, 0, "추가하기", "#FFFFFF"))
                        add(Category(-1, 0, "선택하세요", "#FFFFFF"))
                    }

                    val adapter =
                        CustomCategorySpinnerAdapter(requireContext(), categorySpinnerList)

                    binding.spinnerCategory.adapter = adapter
                    var inx = 0
                    (0 until categorySpinnerList.size).forEach {
                        if (editHistoryViewModel.selectedCategoryId == categorySpinnerList[it].id) inx =
                            it
                    }
                    binding.spinnerCategory.setSelection(inx)
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
                                    when(editHistoryViewModel.categoryType) {
                                        0 -> mainViewModel.moveToIncomeCategoryFragment()
                                        1 -> mainViewModel.moveToExpenditureCategoryFragment()
                                    }
                                } else {
                                    adapterView?.findViewById<TextView>(R.id.tv_content)?.text =
                                        categorySpinnerList[position].content
                                    editHistoryViewModel.selectedCategoryId =
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

    private fun collectButtonActivate() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                editHistoryViewModel.buttonActiveFlow.collect {
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

    private fun collectUpdateHistorySuccess() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                editHistoryViewModel.updateHistorySuccessful.collect {
                    if (it) {
                        Toast.makeText(requireContext(), "내역이 수정되었습니다", Toast.LENGTH_SHORT)
                            .show()
                        mainViewModel.pressBackButtonInAppBar()
                    } else {
                        Toast.makeText(requireContext(), "내역 수정에 실패했습니다", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        }
    }

    inner class LeftListener : CustomAppBar.LeftImageClickListener {
        override fun clickLeft(view: View) {
            printLog("${this.javaClass.simpleName}/ back clicked")
            binding.etContent.setText("")
            mainViewModel.pressBackButtonInAppBar()
        }
    }
}
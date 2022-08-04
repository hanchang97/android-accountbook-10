package com.nimok97.accountbook.presentation.setting.expenditure.edit

import androidx.compose.ui.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.nimok97.accountbook.R
import com.nimok97.accountbook.common.printLog
import com.nimok97.accountbook.databinding.FragmentExpenditureCategoryEditBinding
import com.nimok97.accountbook.presentation.MainViewModel
import com.nimok97.accountbook.presentation.setting.SettingViewModel
import com.nimok97.accountbook.presentation.theme.Category_Expenditure_Color_List
import com.nimok97.accountbook.presentation.theme.Primary_off_white
import com.nimok97.accountbook.presentation.util.CustomAppBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EditExpenditureCategoryFragment : Fragment() {

    private lateinit var binding: FragmentExpenditureCategoryEditBinding
    private val editExpenditureCategoryViewModel: EditExpenditureCategoryViewModel by viewModels()
    private val mainViewModel: MainViewModel by activityViewModels()
    private val settingViewModel: SettingViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        printLog("${this.javaClass.simpleName} / onCreateView")
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_expenditure_category_edit,
            container,
            false
        )
        binding.editExpenditureCategoryViewModel = editExpenditureCategoryViewModel
        binding.lifecycleOwner = this.viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
    }

    private fun initView() {
        setSelectedData()
        setAppBar()
        setEditText()
        setComposeView()
        collectData()
    }

    private fun setSelectedData() {
        editExpenditureCategoryViewModel.curerntCategoryId =
            settingViewModel.selectedCategoryForEdit.id
        editExpenditureCategoryViewModel.content = settingViewModel.selectedCategoryForEdit.content
        editExpenditureCategoryViewModel.selectedContent = settingViewModel.selectedCategoryForEdit.content
        editExpenditureCategoryViewModel.selectedColorString =
            settingViewModel.selectedCategoryForEdit.color

        binding.etContent.setText(editExpenditureCategoryViewModel.content)
    }

    private fun setAppBar() {
        binding.customAppBar.setOnLeftImageClickListener(LeftListener())
    }

    private fun setEditText() {
        binding.etContent.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                printLog("${s.toString().isEmpty()}")
                editExpenditureCategoryViewModel.checkContentEmpty(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun setComposeView() {
        binding.composeView.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                Surface(
                    color = Primary_off_white,
                    modifier = Modifier.fillMaxSize()
                ) {
                    ComposeViewSetting()
                }
            }
        }
    }

    @Composable
    fun ComposeViewSetting() {
        Column {
            Spacer(modifier = Modifier.height(16.dp))
            CategoryColorList()
        }
    }

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    fun CategoryColorList() {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp, 0.dp)
        ) {

            var selectedValue =
                remember { mutableStateOf(editExpenditureCategoryViewModel.selectedColorString) }
            val changeSelectedColor = { color: String -> selectedValue.value = color }

            LazyVerticalGrid(
                cells = GridCells.Fixed(10),
                Modifier.fillMaxWidth()
            ) {
                items(Category_Expenditure_Color_List) { color ->
                    val intColor = android.graphics.Color.parseColor(color)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.1f)
                            .aspectRatio(1 / 1f)
                            .background(Color(intColor))
                            .border(
                                width = if (color == selectedValue.value) 5.dp else 10.dp,
                                color = Primary_off_white
                            )
                            .clickable {
                                changeSelectedColor(color)
                                editExpenditureCategoryViewModel.setCurrentColor(color)
                            }
                    ) {

                    }
                }
            }
        }
    }

    private fun collectData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                editExpenditureCategoryViewModel.contentFlow.collect {
                    changeButtonState(it)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                editExpenditureCategoryViewModel.contentAlreadyExist.collect {
                    if (it) Toast.makeText(requireContext(), "이미 존재하는 카테고리 입니다", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                editExpenditureCategoryViewModel.updateCategorySuccessful.collect {
                    if (it) {
                        Toast.makeText(requireContext(), "지출 카테고리가 수정되었습니다", Toast.LENGTH_SHORT)
                            .show()
                        mainViewModel.pressBackButtonInAppBar()
                    } else {
                        Toast.makeText(requireContext(), "지출 카테고리 수정에 실패했습니다", Toast.LENGTH_SHORT)
                            .show()
                    }
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

    inner class LeftListener : CustomAppBar.LeftImageClickListener {
        override fun clickLeft(view: View) {
            printLog("${this.javaClass.simpleName}/ back clicked")
            mainViewModel.pressBackButtonInAppBar()
        }
    }
}
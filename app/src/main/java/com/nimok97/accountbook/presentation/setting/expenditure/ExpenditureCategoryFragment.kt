package com.nimok97.accountbook.presentation.setting.expenditure

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.unit.dp
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.nimok97.accountbook.R
import com.nimok97.accountbook.common.printLog
import com.nimok97.accountbook.databinding.FragmentExpenditureCategoryBinding
import com.nimok97.accountbook.presentation.MainViewModel
import com.nimok97.accountbook.presentation.theme.Category_Expenditure_Color_List
import com.nimok97.accountbook.presentation.theme.Primary_off_white
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ExpenditureCategoryFragment : Fragment() {

    private lateinit var binding: FragmentExpenditureCategoryBinding
    private val mainViewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        printLog("ExpenditureCategoryFragment / onCreateView")
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_expenditure_category,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
    }

    private fun initView() {
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

        var selectedValue = remember { mutableStateOf(0xFF4A6CC3) }
        val changeSelectedColor = { color: Long -> selectedValue.value = color }

        LazyVerticalGrid(
            cells = GridCells.Fixed(10),
            Modifier.fillMaxWidth()
        ) {
            items(Category_Expenditure_Color_List) { color ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.1f)
                        .aspectRatio(1 / 1f)
                        .background(Color(color))
                        .border(
                            width = if (color == selectedValue.value) 6.dp else 10.dp,
                            color = Primary_off_white
                        )
                        .clickable {
                            changeSelectedColor(color)
                        }
                ) {

                }
            }
        }
    }
}
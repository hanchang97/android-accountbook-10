package com.nimok97.accountbook.presentation.setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.nimok97.accountbook.R
import com.nimok97.accountbook.common.printLog
import com.nimok97.accountbook.data.dao.CategoryDao
import com.nimok97.accountbook.data.dao.MethodDao
import com.nimok97.accountbook.databinding.FragmentSettingBinding
import com.nimok97.accountbook.presentation.theme.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingFragment : Fragment() {

    private lateinit var binding: FragmentSettingBinding
    private val settingViewModel: SettingViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        printLog("SettingFragment / onCreateView")
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_setting, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //saveDefaultData()

        initView()
    }

    private fun saveDefaultData() {
        saveDefaultCategory()
        saveDefaultMethod()
    }

    private fun saveDefaultMethod() { // 기본 데이터
        // 결제수단
        settingViewModel.addMethod(MethodDao("현대카드"))
        settingViewModel.addMethod(MethodDao("카카오뱅크 체크카드"))
    }

    private fun saveDefaultCategory() { // 기본 데이터
        // 수입
        settingViewModel.addCategory(CategoryDao(0, "월급", "#9BD182"))
        settingViewModel.addCategory(CategoryDao(0, "용돈", "#EDCF65"))
        settingViewModel.addCategory(CategoryDao(0, "기타", "#E29C4D"))

        //지출
        settingViewModel.addCategory(CategoryDao(1, "교통", "#94D3CC"))
        settingViewModel.addCategory(CategoryDao(1, "문화/여가", "#D092E2"))
        settingViewModel.addCategory(CategoryDao(1, "미분류", "#817DCE"))
        settingViewModel.addCategory(CategoryDao(1, "생활", "#4A6CC3"))
        settingViewModel.addCategory(CategoryDao(1, "쇼핑/뷰티", "#4CB8B8"))
        settingViewModel.addCategory(CategoryDao(1, "식비", "#4CA1DE"))
        settingViewModel.addCategory(CategoryDao(1, "의료/건강", "#6ED5EB"))
    }

    private fun initView() {
        settingViewModel.getAllCategory()
        settingViewModel.getAllMethod()

        binding.composeView.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                Surface(
                    color = Primary_off_white
                ) {
                    SettingView()
                }
            }
        }
    }

    @Composable
    fun SettingView() {
        LazyColumn() {
            item { Header(title = "결제수단") }
            item { Footer(title = "결제수단 추가하기") }

            item { Header(title = "지출 카테고리") }
            item { Footer(title = "지출 카테고리 추가하기") }

            item { Header(title = "수입 카테고리") }
            item { Footer(title = "수입 카테고리 추가하기") }
        }
    }

    @Composable
    fun Header(title: String) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = title,
                style = TextStyle(
                    color = Primary_light_purple,
                    fontFamily = kopubworld_dotum_pro,
                    fontWeight = FontWeight.Medium
                ),
                fontSize = 16.sp,
                modifier = Modifier
                    .padding(16.dp, 0.dp, 0.dp, 0.dp)
                    .fillMaxWidth(),
                textAlign = TextAlign.Start
            )
            Spacer(modifier = Modifier.height(8.dp))
            Divider(
                modifier = Modifier
                    .fillMaxWidth(.92f)
                    .height(1.dp),
                color = Primary_purple_40
            )
        }
    }

    @Composable
    fun Footer(title: String) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Spacer(modifier = Modifier.height(11.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp, 0.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = title,
                    style = TextStyle(
                        color = Primary_purple,
                        fontFamily = kopubworld_dotum_pro,
                        fontWeight = FontWeight.Bold
                    ),
                    fontSize = 14.sp,
                    //modifier = Modifier.weight(1f)
                )
                Image(
                    painter = painterResource(id = R.drawable.ic_plus_purple),
                    contentDescription = "plus method / category"
                )
            }
            Spacer(modifier = Modifier.height(11.dp))
            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp),
                color = Primary_light_purple
            )
        }
    }
}
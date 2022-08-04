package com.nimok97.accountbook.presentation.setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.nimok97.accountbook.R
import com.nimok97.accountbook.common.printLog
import com.nimok97.accountbook.data.dao.CategoryDao
import com.nimok97.accountbook.data.dao.MethodDao
import com.nimok97.accountbook.databinding.FragmentSettingBinding
import com.nimok97.accountbook.domain.model.Category
import com.nimok97.accountbook.domain.model.Method
import com.nimok97.accountbook.presentation.MainViewModel
import com.nimok97.accountbook.presentation.theme.*
import com.nimok97.accountbook.presentation.util.CATEGORY_EXPENDITURE
import com.nimok97.accountbook.presentation.util.CATEGORY_INCOME
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingFragment : Fragment() {

    private lateinit var binding: FragmentSettingBinding
    private val settingViewModel: SettingViewModel by activityViewModels()
    //private val settingViewModel: SettingViewModel by viewModels()
    // viewModels() 로 설정한 경우 다른 탭으로 갔다가 다시 설정 탭으로 왔을 때 viewModelScope.launch 내부 호출이 되지 않았음
    // why???
    private val mainViewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        printLog("${this.javaClass.simpleName} / onCreateView")
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_setting, container, false)
        binding.mainViewModel = mainViewModel
        binding.lifecycleOwner = this.viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        printLog("${this.javaClass.simpleName} / onViewCreated")
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
        printLog("SettingFragment / initView Called")
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
        val methodListState = settingViewModel.methodistFlow.collectAsState().value
        val categoryIncomeState = settingViewModel.categoryIncomeListFlow.collectAsState().value
        val categoryExpenditureState =
            settingViewModel.categoryExpenditureListFlow.collectAsState().value
        LazyColumn() {
            item { Header(title = "결제수단") }
            //item { ContentMehtod(method = Method(10, "결제수단 테스트")) }
            items(methodListState) { method ->
                ContentMehtod(method)
            }
            item { Footer(title = "결제수단 추가하기", 1) }

            item { Header(title = "지출 카테고리") }
            //item { ContentCategory(category = Category(100, 1, "테스트", "#4A6CC3")) }
            items(categoryExpenditureState) { category ->
                ContentCategory(category)
            }
            item { Footer(title = "지출 카테고리 추가하기", 2) }

            item { Header(title = "수입 카테고리") }
            items(categoryIncomeState) { category ->
                ContentCategory(category)
            }
            item { Footer(title = "수입 카테고리 추가하기", 3) }
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
    fun Footer(title: String, footerType: Int) {
        Column(modifier = Modifier
            .fillMaxWidth()
            .clickable {
                when(footerType){
                    1 -> {
                        mainViewModel.moveToMethodFragment()
                    }
                    2 -> {
                        mainViewModel.moveToExpenditureCategoryFragment()
                    }
                    else -> {

                    }
                }
            }) {
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

    @Composable
    fun ContentMehtod(method: Method) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp, 0.dp)
        ) {
            Spacer(modifier = Modifier.height(11.dp))
            Text(
                text = method.content,
                style = TextStyle(
                    color = Primary_purple,
                    fontFamily = kopubworld_dotum_pro,
                    fontWeight = FontWeight.Bold
                ),
                fontSize = 14.sp,
            )
            Spacer(modifier = Modifier.height(11.dp))
            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp),
                color = Primary_purple_40
            )
        }
    }

    @Composable
    fun ContentCategory(category: Category) {
        val intColor = android.graphics.Color.parseColor(category.color)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp, 0.dp)
                .clickable {
                    settingViewModel.selectedCategoryForEdit = category
                    when(category.type) {
                        CATEGORY_EXPENDITURE -> {
                            mainViewModel.moveToEditExpenditureCategoryFragment()
                        }
                        else -> {
                            // 수입 수정 이동
                        }
                    }
                }
        ) {
            Spacer(modifier = Modifier.height(11.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = category.content,
                    style = TextStyle(
                        color = Primary_purple,
                        fontFamily = kopubworld_dotum_pro,
                        fontWeight = FontWeight.Bold
                    ),
                    fontSize = 14.sp
                )
                Card(
                    shape = RoundedCornerShape(50),
                    backgroundColor = Color(intColor),
                    elevation = 0.dp
                ) {
                    Text(
                        text = category.content,
                        modifier = Modifier
                            .defaultMinSize(56.dp, 0.dp)
                            .padding(4.dp),
                        style = TextStyle(
                            color = White,
                            fontFamily = kopubworld_dotum_pro,
                            fontWeight = FontWeight.Bold
                        ),
                        fontSize = 10.sp,
                        textAlign = TextAlign.Center
                    )
                }
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
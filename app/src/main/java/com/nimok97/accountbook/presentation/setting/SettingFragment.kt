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
    // viewModels() ??? ????????? ?????? ?????? ????????? ????????? ?????? ?????? ????????? ?????? ??? viewModelScope.launch ?????? ????????? ?????? ?????????
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

    private fun saveDefaultMethod() { // ?????? ?????????
        // ????????????
        settingViewModel.addMethod(MethodDao("????????????"))
        settingViewModel.addMethod(MethodDao("??????????????? ????????????"))
    }

    private fun saveDefaultCategory() { // ?????? ?????????
        // ??????
        settingViewModel.addCategory(CategoryDao(0, "??????", "#9BD182"))
        settingViewModel.addCategory(CategoryDao(0, "??????", "#EDCF65"))
        settingViewModel.addCategory(CategoryDao(0, "??????", "#E29C4D"))

        //??????
        settingViewModel.addCategory(CategoryDao(1, "??????", "#94D3CC"))
        settingViewModel.addCategory(CategoryDao(1, "??????/??????", "#D092E2"))
        settingViewModel.addCategory(CategoryDao(1, "?????????", "#817DCE"))
        settingViewModel.addCategory(CategoryDao(1, "??????", "#4A6CC3"))
        settingViewModel.addCategory(CategoryDao(1, "??????/??????", "#4CB8B8"))
        settingViewModel.addCategory(CategoryDao(1, "??????", "#4CA1DE"))
        settingViewModel.addCategory(CategoryDao(1, "??????/??????", "#6ED5EB"))
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
            item { Header(title = "????????????") }
            //item { ContentMehtod(method = Method(10, "???????????? ?????????")) }
            items(methodListState) { method ->
                ContentMehtod(method)
            }
            item { Footer(title = "???????????? ????????????", 1) }

            item { Header(title = "?????? ????????????") }
            //item { ContentCategory(category = Category(100, 1, "?????????", "#4A6CC3")) }
            items(categoryExpenditureState) { category ->
                ContentCategory(category)
            }
            item { Footer(title = "?????? ???????????? ????????????", 2) }

            item { Header(title = "?????? ????????????") }
            items(categoryIncomeState) { category ->
                ContentCategory(category)
            }
            item { Footer(title = "?????? ???????????? ????????????", 3) }
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
                        mainViewModel.moveToIncomeCategoryFragment()
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
                            // ?????? ?????? ??????
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
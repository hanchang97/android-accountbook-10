package com.nimok97.accountbook.presentation.setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.nimok97.accountbook.R
import com.nimok97.accountbook.common.printLog
import com.nimok97.accountbook.data.dao.CategoryDao
import com.nimok97.accountbook.data.dao.MethodDao
import com.nimok97.accountbook.databinding.FragmentSettingBinding
import com.nimok97.accountbook.domain.model.Category
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
        //saveDefaultMethod()

        initView()
    }

    private fun saveDefaultData(){
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
    }
}
package com.nimok97.accountbook.presentation.setting.method

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
import com.nimok97.accountbook.databinding.FragmentMethodBinding
import com.nimok97.accountbook.presentation.MainViewModel
import com.nimok97.accountbook.presentation.util.CustomAppBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MethodFragment : Fragment() {

    private lateinit var binding: FragmentMethodBinding
    private val mainViewModel: MainViewModel by activityViewModels()
    private val methodViewModel: MethodViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        printLog("${this.javaClass.simpleName} / onCreateView")
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_method, container, false)
        binding.methodViewModel = methodViewModel
        binding.lifecycleOwner = this.viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        printLog("${this.javaClass.simpleName} / onViewCreated")

        initView()
        collectData()
    }

    private fun initView() {
        setAppBar()
        setEditText()
    }

    private fun setAppBar() {
        binding.customAppBar.setOnLeftImageClickListener(LeftListener())
    }

    private fun setEditText() {
        binding.etContent.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                printLog("${s.toString().isEmpty()}")
                methodViewModel.checkContentEmpty(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })
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

    private fun collectData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                methodViewModel.contentFlow.collect {
                    changeButtonState(it)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                methodViewModel.contentAlreadyExist.collect {
                    if (it) Toast.makeText(requireContext(), "이미 존재하는 결제수단 입니다", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                methodViewModel.addMethodSuccessful.collect {
                    if (it) {
                        Toast.makeText(requireContext(), "결제수단이 추가되었습니다", Toast.LENGTH_SHORT)
                            .show()
                        mainViewModel.pressBackInMethodFragment()
                    } else {
                        Toast.makeText(requireContext(), "결제수단 추가에 실패했습니다", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        }
    }

    inner class LeftListener : CustomAppBar.LeftImageClickListener {
        override fun clickLeft(view: View) {
            printLog("MethodFragment/ back clicked")
            binding.etContent.setText("")
            mainViewModel.pressBackInMethodFragment()
        }
    }
}
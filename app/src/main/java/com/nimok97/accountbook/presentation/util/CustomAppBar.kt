package com.nimok97.accountbook.presentation.util

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.nimok97.accountbook.R

class CustomAppBar(context: Context, attrs: AttributeSet) : ConstraintLayout(context, attrs) {

    private var showLeftButton: Boolean = true
    private var showRightButton: Boolean = true
    private var leftImage: Int = 0
    private var rightImage: Int = 0
    private lateinit var title: String

    init {
        initAttrs(attrs)
        initView()
    }

    private fun initAttrs(attrs: AttributeSet) {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.CustomAppBar,
            0, 0
        ).apply {
            // 속성으로 전달받은 값을 대입하는 부분
            try {
                showLeftButton = getBoolean(R.styleable.CustomAppBar_showLeftButton, true)
                showRightButton = getBoolean(R.styleable.CustomAppBar_showRightButton, true)
                leftImage = getResourceId(R.styleable.CustomAppBar_leftImage, 0)
                rightImage = getResourceId(R.styleable.CustomAppBar_rightImage, 0)
                title = getString(R.styleable.CustomAppBar_title) ?: ""
            } finally {
                recycle()
            }
        }
    }

    private fun initView() {
        inflate(context, R.layout.custom_view_appbar, this)

        val titleTextView = findViewById<TextView>(R.id.custom_tv_title)
        val leftImageView = findViewById<ImageView>(R.id.custom_iv_left)
        val rightImageView = findViewById<ImageView>(R.id.custom_iv_right)

        titleTextView.text = title
        leftImageView.setImageResource(leftImage)
        rightImageView.setImageResource(rightImage)
        if(!showLeftButton) leftImageView.visibility = View.GONE
        if(!showRightButton) rightImageView.visibility = View.GONE
    }



}
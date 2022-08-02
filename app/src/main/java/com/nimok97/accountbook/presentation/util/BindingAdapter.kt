package com.nimok97.accountbook.presentation.util

import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.nimok97.accountbook.R
import java.text.DecimalFormat

@BindingAdapter("priceAmount", "historyType")
fun applyPriceFormatByType(view: TextView, price: Int?, historyType: Int?) {
    val decimalFormat = DecimalFormat("#,###")
    var amountStr = view.context.getString(R.string.content_price, decimalFormat.format(price))

    if (historyType == 0) {
        view.text = amountStr
        view.setTextColor(ContextCompat.getColor(view.context, R.color.income))
    } else {
        amountStr = "-$amountStr"
        view.text = amountStr
        view.setTextColor(ContextCompat.getColor(view.context, R.color.expenditure))
    }
}

@BindingAdapter("totalPriceAmount")
fun applyPriceFormat(view: TextView, price: Int?) {
    val decimalFormat = DecimalFormat("#,###")
    var amountStr = decimalFormat.format(price)
    view.text = amountStr
}
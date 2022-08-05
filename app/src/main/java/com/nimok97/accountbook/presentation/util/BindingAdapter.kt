package com.nimok97.accountbook.presentation.util

import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import com.nimok97.accountbook.R
import com.nimok97.accountbook.common.defaultDateString
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

@BindingAdapter("calendarIncomePrice")
fun applyCalendarIncomePrice(view: TextView, price: Int?) {
    val decimalFormat = DecimalFormat("#,###")
    var amountStr = decimalFormat.format(price)
    view.text = amountStr
    view.isVisible = price != 0
}

@BindingAdapter("calendarExpenditurePrice")
fun applyCalendarExoenditurePrice(view: TextView, price: Int?) {
    val decimalFormat = DecimalFormat("#,###")
    var amountStr = decimalFormat.format(price)
    view.text = "-$amountStr"
    view.isVisible = price != 0
}

@BindingAdapter("calendarTotalIncomePrice")
fun applyCalendarTotalIncomePrice(view: TextView, price: Int?) {
    val decimalFormat = DecimalFormat("#,###")
    var amountStr = decimalFormat.format(price)
    view.text = amountStr
}

@BindingAdapter("calendarTotalExpenditurePrice")
fun applyCalendarTotalExoenditurePrice(view: TextView, price: Int?) {
    if (price == 0) view.text = "0"
    else {
        val decimalFormat = DecimalFormat("#,###")
        var amountStr = decimalFormat.format(price)
        view.text = "-$amountStr"
    }
}

@BindingAdapter("calendarDayColor")
fun applyCalendarDayColor(view: TextView, isCurrentMonth: Boolean) {
    if (isCurrentMonth) view.setTextColor(
        ContextCompat.getColor(
            view.context,
            R.color.primary_purple
        )
    )
    else view.setTextColor(ContextCompat.getColor(view.context, R.color.primary_purple_40))
}

@BindingAdapter("checkedState")
fun applyBackground(view: ConstraintLayout, checked: Boolean) {
    if (checked) view.setBackgroundColor(
        ContextCompat.getColor(
            view.context,
            R.color.primary_purple
        )
    ) else view.setBackgroundColor(
        ContextCompat.getColor(
            view.context,
            R.color.primary_light_purple
        )
    )
}

@BindingAdapter("dateSelected")
fun applyDateSelected(view: TextView, dateStr: String) {
    if (dateStr == defaultDateString) view.setTextColor(
        ContextCompat.getColor(
            view.context,
            R.color.primary_light_purple
        )
    ) else
        view.setTextColor(
            ContextCompat.getColor(
                view.context,
                R.color.primary_purple
            )
        )
}

@BindingAdapter("statisticsVisibility")
fun applyStatisticsVisibility(view: View, empty: Boolean) {
   view.isVisible = !empty
}

@BindingAdapter("emptyTextVisibility")
fun applyEmptyTextVisibility(view: View, empty: Boolean) {
    view.isVisible = empty
}
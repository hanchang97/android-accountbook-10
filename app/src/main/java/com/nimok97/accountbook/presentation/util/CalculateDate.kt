package com.nimok97.accountbook.presentation.util

import com.nimok97.accountbook.common.printLog
import java.text.SimpleDateFormat
import java.util.*

fun calculateCurrentYear(){
    val currentTime = Calendar.getInstance().time
    val yearFormat = SimpleDateFormat("yyyy", Locale.KOREA)
    val yearStr = yearFormat.format(currentTime)

    printLog("current year : $yearStr")
}

fun calculateCurrentMonth(){
    val currentTime = Calendar.getInstance().time
    val monthFormat = SimpleDateFormat("MM", Locale.KOREA)
    val monthStr = monthFormat.format(currentTime)

    printLog("current month : $monthStr")
}
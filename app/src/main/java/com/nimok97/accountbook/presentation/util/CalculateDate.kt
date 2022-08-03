package com.nimok97.accountbook.presentation.util

import android.os.Build
import androidx.annotation.RequiresApi
import com.nimok97.accountbook.common.printLog
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.*

fun calculateCurrentYear(): Int {
    val currentTime = Calendar.getInstance().time
    val yearFormat = SimpleDateFormat("yyyy", Locale.KOREAN)
    val yearStr = yearFormat.format(currentTime)

    printLog("current year : $yearStr")
    return yearStr.toInt()
}

fun calculateCurrentMonth(): Int {
    val currentTime = Calendar.getInstance().time
    val monthFormat = SimpleDateFormat("MM", Locale.KOREAN)
    val monthStr = monthFormat.format(currentTime)

    printLog("current month : $monthStr")
    return monthStr.toInt()
}

@RequiresApi(Build.VERSION_CODES.O)
fun calculateDayString(year: Int, month: Int, day: Int): String {
    val date = LocalDate.of(year, month, day)
    val dayOfWeek = date.dayOfWeek

    return dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.KOREAN)
}
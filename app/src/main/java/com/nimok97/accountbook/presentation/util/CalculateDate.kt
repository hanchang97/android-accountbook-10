package com.nimok97.accountbook.presentation.util

import android.os.Build
import androidx.annotation.RequiresApi
import com.nimok97.accountbook.common.printLog
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.*

fun calculateCurrentYear(): Int {
    val currentTime = Calendar.getInstance().time
    val yearFormat = SimpleDateFormat("yyyy", Locale.KOREAN)
    val yearStr = yearFormat.format(currentTime)

    printLog("current year : ${yearStr.toInt()}")
    return yearStr.toInt()
}

fun calculateCurrentMonth(): Int {
    val currentTime = Calendar.getInstance().time
    val monthFormat = SimpleDateFormat("MM", Locale.KOREAN)
    val monthStr = monthFormat.format(currentTime)

    printLog("current month : ${monthStr.toInt()}")
    return monthStr.toInt()
}

fun calculateCurrentDay(): Int {
    val currentTime = Calendar.getInstance().time
    val dayFormat = SimpleDateFormat("dd", Locale.KOREAN)
    val dayStr = dayFormat.format(currentTime)

    printLog("current month : ${dayStr.toInt()}")
    return dayStr.toInt()
}

@RequiresApi(Build.VERSION_CODES.O)
fun calculateDayString(year: Int, month: Int, day: Int): String {
    val date = LocalDate.of(year, month, day)
    val dayOfWeek = date.dayOfWeek

    return dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.KOREAN)
}

@RequiresApi(Build.VERSION_CODES.O)
fun calculateFirstDayOfMonth(year: Int, month: Int) {
    //val currentDate = LocalDate.now()
    val currentDate = LocalDate.of(year, month, 1)
    var yearMonth = YearMonth.from(currentDate)

    var firstDay = currentDate.withDayOfMonth(1)
    var lastDay = yearMonth.lengthOfMonth()

    var dayOfWeek = firstDay.dayOfWeek.value // 월요일 = 1, 일요일 = 7

    printLog("firstDay : $firstDay")
    printLog("lastDay : $lastDay")
    printLog("dayOfWeek : $dayOfWeek")
}

@RequiresApi(Build.VERSION_CODES.O)
fun calculateCurrentMonthDayCount(year: Int, month: Int): Int {
    val currentDate = LocalDate.of(year, month, 1)
    var yearMonth = YearMonth.from(currentDate)

    return yearMonth.lengthOfMonth()
}

@RequiresApi(Build.VERSION_CODES.O)
fun calculateCurrentMonthStartDay(year: Int, month: Int): Int {
    val currentDate = LocalDate.of(year, month, 1)
    var firstDay = currentDate.withDayOfMonth(1)

    return firstDay.dayOfWeek.value
}
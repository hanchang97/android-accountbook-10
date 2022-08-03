package com.nimok97.accountbook.common

import android.util.Log

fun printLog(msg: String) {
    Log.e("AppTest", msg)
}

val defaultDateString = "날짜를 선택하세요"

fun getDateString(year: Int, month: Int, dayNum: Int, dayStr: String) =
    "$year. $month. $dayNum ${dayStr}요일"
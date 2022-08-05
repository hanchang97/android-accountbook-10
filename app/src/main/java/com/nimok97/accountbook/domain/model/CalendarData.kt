package com.nimok97.accountbook.domain.model

data class CalendarData(
    var income: Int = 0,
    var expenditure: Int = 0,
    var total: Int = 0,
    var year: Int = 0,
    var month: Int = 0,
    var day: Int = 0,
    var isCurrentMonth: Boolean = false
)

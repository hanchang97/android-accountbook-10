package com.nimok97.accountbook.domain.model

data class CategoryStatistics(
    var categoryId: Int = -1,
    var content: String = "",
    var color: String = "",
    var amount: Int = 0,
    var percent: Float = 0f
)

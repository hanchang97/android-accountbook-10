package com.nimok97.accountbook.data.dao

data class HistoryDao(
    val type: Int, // 0 = μμ, 1 = μ§μΆ
    val year: Int,
    val month: Int,
    val dayNum: Int,
    val dayStr: String,
    val amount: Int,
    val content: String,
    val methodId: Int,
    val categoryId: Int
)

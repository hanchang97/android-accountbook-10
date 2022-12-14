package com.nimok97.accountbook.domain.model

data class History(
    val id: Int,
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

package com.nimok97.accountbook.domain.model

data class Category(
    val id: Int,
    val type: Int,
    val content: String,
    val color: String = "#000000"
)

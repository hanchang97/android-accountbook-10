package com.nimok97.accountbook.domain.model

data class HistoryItem(
    var viewType: String? = "content",
    var history: History? = null,
    var category: Category? = null,
    var method: Method? = null,
    var isLastItem: Boolean? = false,
    var isCheckVisible: Boolean? = false,
    var isChecked: Boolean? = false
)
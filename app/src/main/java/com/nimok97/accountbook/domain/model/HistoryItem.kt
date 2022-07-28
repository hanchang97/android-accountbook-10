package com.nimok97.accountbook.domain.model

data class HistoryItem(
    var viewType: String?,
    var history: History?,
    var category: Category?,
    var method: Method?,
    var isLastItem: Boolean?,
    var isCheckVisible: Boolean? = false,
    var isChecked: Boolean? = false
)
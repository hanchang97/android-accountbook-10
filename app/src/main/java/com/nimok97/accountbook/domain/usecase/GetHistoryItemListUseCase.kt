package com.nimok97.accountbook.domain.usecase

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async

class GetHistoryItemListUseCase(
    private val getAllHistoryByYearAndMonthUseCase: GetAllHistoryByYearAndMonthUseCase,
    private val getCategoryByIdUseCase: GetCategoryByIdUseCase,
    private val getMethodByIdUseCase: GetMethodByIdUseCase
) {

    suspend fun getHistoryItemList(year: Int, month: Int){
        val historyResult = getAllHistoryByYearAndMonthUseCase.getAllHistoryByYearAndMonth(year, month)
        when{
            historyResult.isSuccess -> {

            }
            historyResult.isFailure -> {

            }
        }
    }
}
package com.nimok97.accountbook.domain.usecase

import com.nimok97.accountbook.common.printLog
import com.nimok97.accountbook.domain.repository.HistoryRepository

class GetAllHistoryByYearAndMonthUseCase(
    private val historyRepository: HistoryRepository
) {
    suspend fun getAllHistoryByYearAndMonth(year: Int, month: Int){
        val result = historyRepository.getAllHistory(year, month)
        when{
            result.isSuccess -> {
                result.getOrNull()?.let {
                    it.forEach {
                        printLog("$it")
                    }
                }
            }
            result.isFailure -> {
                printLog("get all history fail")
            }
        }
    }
}
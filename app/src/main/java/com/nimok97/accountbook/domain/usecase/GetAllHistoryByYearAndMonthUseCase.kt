package com.nimok97.accountbook.domain.usecase

import com.nimok97.accountbook.common.printLog
import com.nimok97.accountbook.domain.model.History
import com.nimok97.accountbook.domain.repository.HistoryRepository

class GetAllHistoryByYearAndMonthUseCase(
    private val historyRepository: HistoryRepository
) {
    suspend fun getAllHistoryByYearAndMonth(year: Int, month: Int): Result<List<History>>{
        val result = historyRepository.getAllHistory(year, month)
        when{
            result.isSuccess -> {
                printLog("get all history success")
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
        return result
    }
}
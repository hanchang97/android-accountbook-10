package com.nimok97.accountbook.domain.usecase

import com.nimok97.accountbook.common.printLog
import com.nimok97.accountbook.data.dao.HistoryDao
import com.nimok97.accountbook.domain.repository.HistoryRepository

class AddHistoryUseCase(
    private val historyRepository: HistoryRepository
) {
    suspend fun addHistory(historyDao: HistoryDao): Result<Long> {
        val result = historyRepository.addHistory(historyDao)
        when {
            result.isSuccess -> {
                printLog("add history success")
            }
            result.isFailure -> {
                printLog("add history fail")
            }
        }
        return result
    }
}
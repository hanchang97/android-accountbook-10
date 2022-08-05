package com.nimok97.accountbook.domain.usecase

import com.nimok97.accountbook.common.printLog
import com.nimok97.accountbook.data.dao.HistoryDao
import com.nimok97.accountbook.domain.repository.HistoryRepository

class UpdateHistoryUseCase(
    private val historyRepository: HistoryRepository
) {
    suspend fun updateHistory(id: Int, historyDao: HistoryDao): Result<Int> {
        val result = historyRepository.updateHistory(id, historyDao)
        when {
            result.isSuccess -> {
                printLog("update history success")
            }
            result.isFailure -> {
                printLog("update history fail")
            }
        }
        return result
    }
}
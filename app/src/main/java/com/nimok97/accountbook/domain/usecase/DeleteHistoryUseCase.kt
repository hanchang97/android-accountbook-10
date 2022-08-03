package com.nimok97.accountbook.domain.usecase

import com.nimok97.accountbook.common.printLog
import com.nimok97.accountbook.domain.repository.HistoryRepository

class DeleteHistoryUseCase(
    private val historyRepository: HistoryRepository
) {
    suspend fun deleteHistory(id: Int): Result<Int> {
        val result = historyRepository.deleteHistory(id)
        when {
            result.isSuccess -> {
                printLog("delete history success")
            }
            result.isFailure -> {
                printLog("delete history fail")
            }
        }
        return result
    }
}
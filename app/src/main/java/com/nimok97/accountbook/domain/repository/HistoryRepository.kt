package com.nimok97.accountbook.domain.repository

import com.nimok97.accountbook.data.dao.HistoryDao
import com.nimok97.accountbook.domain.model.History

interface HistoryRepository {
    suspend fun addHistory(historyDao: HistoryDao): Result<Long>
    suspend fun getAllHistory(year: Int, month: Int): Result<List<History>>
    suspend fun updateHistory(id: Int, historyDao: HistoryDao): Result<Int>
    suspend fun deleteHistory(id: Int): Result<Int>
}
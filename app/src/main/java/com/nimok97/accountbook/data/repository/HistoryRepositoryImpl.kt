package com.nimok97.accountbook.data.repository

import com.nimok97.accountbook.data.dao.HistoryDao
import com.nimok97.accountbook.data.datasource.AccountBookDataSource
import com.nimok97.accountbook.domain.model.History
import com.nimok97.accountbook.domain.repository.HistoryRepository

class HistoryRepositoryImpl(
    private val accountBookDataSource: AccountBookDataSource
) : HistoryRepository {

    override suspend fun addHistory(historyDao: HistoryDao): Result<Long> {
        return accountBookDataSource.addHistory(historyDao)
    }

    override suspend fun getAllHistory(year: Int, month: Int): Result<List<History>> {
        return accountBookDataSource.getHistoriesByYearAndMonth(year, month)
    }
}
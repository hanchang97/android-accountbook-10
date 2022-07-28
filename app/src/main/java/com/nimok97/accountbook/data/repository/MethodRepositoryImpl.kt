package com.nimok97.accountbook.data.repository

import com.nimok97.accountbook.data.dao.MethodDao
import com.nimok97.accountbook.data.datasource.AccountBookDataSource
import com.nimok97.accountbook.domain.model.Method
import com.nimok97.accountbook.domain.repository.MethodRepository

class MethodRepositoryImpl(
    private val accountBookDataSource: AccountBookDataSource
) : MethodRepository {

    override suspend fun addMethod(methodDao: MethodDao): Result<Long> {
        return accountBookDataSource.addMethod(methodDao)
    }

    override suspend fun getAllMethod(): Result<List<Method>> {
        return accountBookDataSource.getAllMethod()
    }

}
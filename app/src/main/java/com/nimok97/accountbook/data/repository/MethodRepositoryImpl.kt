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

    override suspend fun getMethodById(methodId: Int): Result<Method> {
        return accountBookDataSource.getMethodById(methodId)
    }

    override suspend fun checkMethodExistenceByContent(content: String): Result<Boolean> {
        return accountBookDataSource.checkMethodExistenceByContent(content)
    }
}
package com.nimok97.accountbook.domain.repository

import com.nimok97.accountbook.data.dao.MethodDao
import com.nimok97.accountbook.domain.model.Method

interface MethodRepository {
    suspend fun addMethod(methodDao: MethodDao): Result<Long>
    suspend fun getAllMethod(): Result<List<Method>>
    suspend fun getMethodById(methodId: Int): Result<Method>
}
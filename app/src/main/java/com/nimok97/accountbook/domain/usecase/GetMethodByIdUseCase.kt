package com.nimok97.accountbook.domain.usecase

import com.nimok97.accountbook.common.printLog
import com.nimok97.accountbook.domain.model.Method
import com.nimok97.accountbook.domain.repository.MethodRepository

class GetMethodByIdUseCase(
    private val methodRepository: MethodRepository
) {
    suspend fun getMethodById(methodId: Int): Result<Method> {
        val result = methodRepository.getMethodById(methodId)
        when {
            result.isSuccess -> {
                printLog("get method by id success")
                result.getOrNull()?.let {
                    printLog("$it")
                }
            }
            result.isFailure -> {
                printLog("get method by id fail")
            }
        }
        return result
    }
}
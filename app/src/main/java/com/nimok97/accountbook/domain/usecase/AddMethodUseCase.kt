package com.nimok97.accountbook.domain.usecase

import com.nimok97.accountbook.common.printLog
import com.nimok97.accountbook.data.dao.MethodDao
import com.nimok97.accountbook.domain.repository.MethodRepository

class AddMethodUseCase(
    private val methodRepository: MethodRepository
) {
    suspend operator fun invoke(methodDao: MethodDao): Result<Long> {
        val result = methodRepository.addMethod(methodDao)
        when {
            result.isSuccess -> {
                printLog("add method success")
            }
            result.isFailure -> {
                printLog("add method fail")
            }
        }
        return result
    }
}
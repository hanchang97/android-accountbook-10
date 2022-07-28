package com.nimok97.accountbook.domain.usecase

import com.nimok97.accountbook.common.printLog
import com.nimok97.accountbook.domain.repository.MethodRepository

class GetAllMethodUseCase(
    private val methodRepository: MethodRepository
) {
    suspend fun getAllMethod(){
        val result = methodRepository.getAllMethod()
        when{
            result.isSuccess -> {
                result.getOrNull()?.let {
                    it.forEach {
                        printLog("$it")
                    }
                }
            }
            result.isFailure -> {

            }
        }
    }
}
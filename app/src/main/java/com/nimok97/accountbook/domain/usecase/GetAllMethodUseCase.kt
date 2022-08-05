package com.nimok97.accountbook.domain.usecase

import com.nimok97.accountbook.domain.model.Method
import com.nimok97.accountbook.domain.repository.MethodRepository

class GetAllMethodUseCase(
    private val methodRepository: MethodRepository
) {
    suspend operator fun invoke(): Result<List<Method>> {
        val result = methodRepository.getAllMethod()
        return result
    }
}
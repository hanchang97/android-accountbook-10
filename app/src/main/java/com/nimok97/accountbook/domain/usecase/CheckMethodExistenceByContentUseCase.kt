package com.nimok97.accountbook.domain.usecase

import com.nimok97.accountbook.domain.repository.MethodRepository

class CheckMethodExistenceByContentUseCase(private val methodRepository: MethodRepository) {
    suspend operator fun invoke(content: String): Result<Boolean> {
        val result = methodRepository.checkMethodExistenceByContent(content)
        return result
    }
}
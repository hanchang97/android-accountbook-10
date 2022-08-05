package com.nimok97.accountbook.domain.usecase

import com.nimok97.accountbook.domain.repository.CategoryRepository

class CheckCategoryExistenceByContentUseCase(private val categoryRepository: CategoryRepository) {
    suspend operator fun invoke(content: String): Result<Boolean> {
        val result = categoryRepository.checkCategoryExistenceByContent(content)
        return result
    }
}
package com.nimok97.accountbook.domain.usecase

import com.nimok97.accountbook.domain.model.Category
import com.nimok97.accountbook.domain.repository.CategoryRepository

class GetAllCategoryUseCase(
    private val categoryRepository: CategoryRepository
) {
    suspend operator fun invoke(): Result<List<Category>>{
        val result = categoryRepository.getAllCategory()
        return result
    }
}
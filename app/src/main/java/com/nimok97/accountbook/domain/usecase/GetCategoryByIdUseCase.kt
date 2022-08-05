package com.nimok97.accountbook.domain.usecase

import com.nimok97.accountbook.common.printLog
import com.nimok97.accountbook.domain.model.Category
import com.nimok97.accountbook.domain.repository.CategoryRepository

class GetCategoryByIdUseCase(
    private val categoryRepository: CategoryRepository
) {
    suspend fun getCategoryById(categoryId: Int): Result<Category> {
        val result = categoryRepository.getCategoryById(categoryId)
        when {
            result.isSuccess -> {
                printLog("get category by id success")
                result.getOrNull()?.let {
                    printLog("$it")
                }
            }
            result.isFailure -> {
                printLog("get category by id fail")
            }
        }
        return result
    }
}
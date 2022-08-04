package com.nimok97.accountbook.domain.usecase

import com.nimok97.accountbook.common.printLog
import com.nimok97.accountbook.data.dao.CategoryDao
import com.nimok97.accountbook.domain.repository.CategoryRepository

class UpdateCategoryUseCase(
    private val categoryRepository: CategoryRepository
) {
    suspend operator fun invoke(id: Int, categoryDao: CategoryDao): Result<Int> {
        val result = categoryRepository.updateCategory(id, categoryDao)
        when {
            result.isSuccess -> {
                printLog("update category success")
            }
            result.isFailure -> {
                printLog("update category fail")
            }
        }
        return result
    }
}
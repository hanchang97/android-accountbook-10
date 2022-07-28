package com.nimok97.accountbook.domain.usecase

import com.nimok97.accountbook.common.printLog
import com.nimok97.accountbook.data.dao.CategoryDao
import com.nimok97.accountbook.domain.repository.CategoryRepository

class AddCategoryUseCase(
    private val categoryRepository: CategoryRepository
) {
    suspend fun addCategory(categoryDao: CategoryDao) {
        val result = categoryRepository.addCategory(categoryDao)
        when {
            result.isSuccess -> {
                printLog("add category success")
            }
            result.isFailure -> {
                printLog("add category fail")
            }
        }
    }
}
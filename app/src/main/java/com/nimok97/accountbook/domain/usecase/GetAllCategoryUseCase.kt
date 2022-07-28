package com.nimok97.accountbook.domain.usecase

import com.nimok97.accountbook.common.printLog
import com.nimok97.accountbook.domain.repository.CategoryRepository

class GetAllCategoryUseCase(
    private val categoryRepository: CategoryRepository
) {
    suspend fun getAllCategory(){
        val result = categoryRepository.getAllCategory()
        when{
            result.isSuccess -> {
                printLog("get all category success")
                result.getOrNull()?.let {
                    it.forEach {
                        printLog("$it")
                    }
                }
            }
            result.isFailure -> {
                printLog("get all category fail")
            }
        }
    }
}
package com.nimok97.accountbook.domain.repository

import com.nimok97.accountbook.data.dao.CategoryDao
import com.nimok97.accountbook.domain.model.Category

interface CategoryRepository {
    suspend fun addCategory(categoryDao: CategoryDao): Result<Long>
    suspend fun getAllCategory(): Result<List<Category>>
    suspend fun getCategoryById(categoryId: Int): Result<Category>
    suspend fun checkCategoryExistenceByContent(content: String): Result<Boolean>
    suspend fun updateCategory(id: Int, categoryDao: CategoryDao): Result<Int>
}
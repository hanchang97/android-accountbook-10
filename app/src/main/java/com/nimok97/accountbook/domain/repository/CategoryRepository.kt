package com.nimok97.accountbook.domain.repository

import com.nimok97.accountbook.data.dao.CategoryDao
import com.nimok97.accountbook.domain.model.Category

interface CategoryRepository {
    suspend fun addCategory(categoryDao: CategoryDao): Result<Long>
    suspend fun getAllCategory(): Result<List<Category>>
}
package com.nimok97.accountbook.data.repository

import com.nimok97.accountbook.data.dao.CategoryDao
import com.nimok97.accountbook.data.datasource.AccountBookDataSource
import com.nimok97.accountbook.domain.model.Category
import com.nimok97.accountbook.domain.repository.CategoryRepository

class CategoryRepositoryImpl(
    private val accountBookDataSource: AccountBookDataSource
) : CategoryRepository {

    override suspend fun addCategory(categoryDao: CategoryDao): Result<Long> {
        return accountBookDataSource.addCategory(categoryDao)
    }

    override suspend fun getAllCategory(): Result<List<Category>> {
        return accountBookDataSource.getAllCategory()
    }

    override suspend fun getCategoryById(categoryId: Int): Result<Category> {
        return accountBookDataSource.getCategoryById(categoryId)
    }

    override suspend fun checkCategoryExistenceByContent(content: String): Result<Boolean> {
        return accountBookDataSource.checkCategoryExistenceByContent(content)
    }

    override suspend fun updateCategory(id: Int, categoryDao: CategoryDao): Result<Int> {
        return accountBookDataSource.updateCategory(id, categoryDao)
    }
}
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

}
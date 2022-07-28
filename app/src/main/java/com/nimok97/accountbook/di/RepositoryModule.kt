package com.nimok97.accountbook.di

import com.nimok97.accountbook.data.datasource.AccountBookDataSource
import com.nimok97.accountbook.data.repository.CategoryRepositoryImpl
import com.nimok97.accountbook.data.repository.MethodRepositoryImpl
import com.nimok97.accountbook.domain.repository.CategoryRepository
import com.nimok97.accountbook.domain.repository.MethodRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Singleton
    @Provides
    fun provideCategoryRepository(
        accountBookDataSource: AccountBookDataSource
    ): CategoryRepository {
        return CategoryRepositoryImpl(accountBookDataSource)
    }

    @Singleton
    @Provides
    fun provideMethodRepository(
        accountBookDataSource: AccountBookDataSource
    ): MethodRepository {
        return MethodRepositoryImpl(accountBookDataSource)
    }

}
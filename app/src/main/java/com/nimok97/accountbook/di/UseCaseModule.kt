package com.nimok97.accountbook.di

import com.nimok97.accountbook.domain.repository.CategoryRepository
import com.nimok97.accountbook.domain.repository.HistoryRepository
import com.nimok97.accountbook.domain.repository.MethodRepository
import com.nimok97.accountbook.domain.usecase.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class UseCaseModule {

    @Singleton
    @Provides
    fun provideAddCategoryUseCase(categoryRepository: CategoryRepository) =
        AddCategoryUseCase(categoryRepository)

    @Singleton
    @Provides
    fun provideGetAllCategoryUseCase(categoryRepository: CategoryRepository) =
        GetAllCategoryUseCase(categoryRepository)

    @Singleton
    @Provides
    fun provideAddMethodUseCase(methodRepository: MethodRepository) =
        AddMethodUseCase(methodRepository)

    @Singleton
    @Provides
    fun provideGetAllMethodUseCase(methodRepository: MethodRepository) =
        GetAllMethodUseCase(methodRepository)

    @Singleton
    @Provides
    fun provideAddHistoryUseCase(historyRepository: HistoryRepository) =
        AddHistoryUseCase(historyRepository)

    @Singleton
    @Provides
    fun provideGetAllHistoryByYearAndMonthUseCase(historyRepository: HistoryRepository) =
        GetAllHistoryByYearAndMonthUseCase(historyRepository)

}
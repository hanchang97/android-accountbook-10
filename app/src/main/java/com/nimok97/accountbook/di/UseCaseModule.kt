package com.nimok97.accountbook.di

import com.nimok97.accountbook.domain.repository.CategoryRepository
import com.nimok97.accountbook.domain.usecase.AddCategoryUseCase
import com.nimok97.accountbook.domain.usecase.GetAllCategoryUseCase
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
    fun provideAddCategoryUseCase(categoryRepository: CategoryRepository) = AddCategoryUseCase(categoryRepository)

    @Singleton
    @Provides
    fun provideGetAllCategoryUseCase(categoryRepository: CategoryRepository) = GetAllCategoryUseCase(categoryRepository)


}
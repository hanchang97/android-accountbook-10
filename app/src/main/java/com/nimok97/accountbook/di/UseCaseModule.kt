package com.nimok97.accountbook.di

import com.nimok97.accountbook.domain.repository.CategoryRepository
import com.nimok97.accountbook.domain.repository.MethodRepository
import com.nimok97.accountbook.domain.usecase.AddCategoryUseCase
import com.nimok97.accountbook.domain.usecase.AddMethodUseCase
import com.nimok97.accountbook.domain.usecase.GetAllCategoryUseCase
import com.nimok97.accountbook.domain.usecase.GetAllMethodUseCase
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

    @Singleton
    @Provides
    fun provideAddMethodUseCase(methodRepository: MethodRepository) = AddMethodUseCase(methodRepository)

    @Singleton
    @Provides
    fun provideGetAllMethodUseCase(methodRepository: MethodRepository) = GetAllMethodUseCase(methodRepository)


}
package com.nimok97.accountbook.di

import android.content.Context
import com.nimok97.accountbook.data.datasource.AccountBookDataSource
import com.nimok97.accountbook.data.datasource.DBHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Singleton
    @Provides
    fun provideDbHelper(@ApplicationContext context: Context) = DBHelper(context)

    @Singleton
    @Provides
    fun provideAccountBookDataSource(dbHelper: DBHelper) = AccountBookDataSource(dbHelper)

}
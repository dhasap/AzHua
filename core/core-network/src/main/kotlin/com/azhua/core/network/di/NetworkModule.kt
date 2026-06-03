package com.azhua.core.network.di

import com.azhua.core.network.AzOkHttpClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    fun provideAzOkHttpClient(): AzOkHttpClient = AzOkHttpClient()
}

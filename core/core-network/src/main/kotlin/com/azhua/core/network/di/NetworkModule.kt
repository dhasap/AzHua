package com.azhua.core.network.di

import com.azhua.core.network.AzOkHttpClient
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    // AzOkHttpClient uses @Inject constructor with @Singleton
    // No @Provides needed - Hilt handles it automatically
}

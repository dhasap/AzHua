package com.azhua.app.extension.di

import com.azhua.app.extension.ExtensionLoader
import com.azhua.app.extension.ExtensionManager
import com.azhua.app.extension.ExtensionRepository
import com.azhua.core.network.AzOkHttpClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ExtensionModule {

    /**
     * Provide OkHttpClient for ExtensionRepository using the app's configured client.
     * This ensures User-Agent and RateLimit interceptors are applied.
     */
    @Provides
    @Singleton
    fun provideExtensionOkHttpClient(azOkHttpClient: AzOkHttpClient): OkHttpClient {
        return azOkHttpClient.create()
    }
}

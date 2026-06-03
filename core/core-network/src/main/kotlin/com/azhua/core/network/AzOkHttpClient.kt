package com.azhua.core.network

import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AzOkHttpClient @Inject constructor() {
    fun create(additionalHeaders: Map<String, String> = emptyMap()): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .followRedirects(true)
            .followSslRedirects(true)
            .addInterceptor(UserAgentInterceptor())
            .addInterceptor(RateLimitInterceptor())
            .apply {
                additionalHeaders.forEach { (key, value) ->
                    addInterceptor { chain ->
                        chain.proceed(
                            chain.request().newBuilder()
                                .header(key, value)
                                .build()
                        )
                    }
                }
            }
            .build()
    }
}

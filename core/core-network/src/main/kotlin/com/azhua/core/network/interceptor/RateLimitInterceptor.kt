package com.azhua.core.network.interceptor

import okhttp3.Interceptor
import okhttp3.Response
import java.util.concurrent.Semaphore
import java.util.concurrent.TimeUnit

/**
 * Rate limiter interceptor to prevent overwhelming sources.
 * Default: max 2 requests per second.
 */
class RateLimitInterceptor(
    private val maxRequests: Int = 2,
    private val perSeconds: Long = 1,
) : Interceptor {
    private val semaphore = Semaphore(maxRequests)

    override fun intercept(chain: Interceptor.Chain): Response {
        try {
            if (!semaphore.tryAcquire(perSeconds, TimeUnit.SECONDS)) {
                // Wait a bit and proceed anyway
                Thread.sleep(500)
            }
            return chain.proceed(chain.request())
        } finally {
            semaphore.release()
        }
    }
}

package com.azhua.ext.anichin.network

import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.util.concurrent.TimeUnit

/**
 * HTTP client for Anichin website.
 * Handles all network requests with proper headers and error handling.
 */
class AnichinClient {

    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .followRedirects(true)
        .followSslRedirects(true)
        .addInterceptor { chain ->
            val request = chain.request().newBuilder()
                .header("User-Agent", USER_AGENT)
                .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
                .header("Accept-Language", "id-ID,id;q=0.9,en-US;q=0.8,en;q=0.7")
                .header("Referer", BASE_URL)
                .build()
            chain.proceed(request)
        }
        .build()

    /**
     * Fetch a page and parse as Jsoup Document.
     */
    fun getDocument(url: String): Document {
        val request = Request.Builder()
            .url(url)
            .get()
            .build()

        val response = client.newCall(request).execute()
        val body = response.body?.string() ?: throw Exception("Empty response body from $url")
        
        if (!response.isSuccessful) {
            throw Exception("HTTP ${response.code} from $url")
        }

        return Jsoup.parse(body, url)
    }

    /**
     * POST form data and parse response as Jsoup Document.
     */
    fun postDocument(url: String, formData: Map<String, String>): Document {
        val formBody = FormBody.Builder().apply {
            formData.forEach { (key, value) -> add(key, value) }
        }.build()

        val request = Request.Builder()
            .url(url)
            .post(formBody)
            .build()

        val response = client.newCall(request).execute()
        val body = response.body?.string() ?: throw Exception("Empty response body from $url")

        if (!response.isSuccessful) {
            throw Exception("HTTP ${response.code} from $url")
        }

        return Jsoup.parse(body, url)
    }

    /**
     * Fetch raw text content from URL.
     */
    fun getText(url: String): String {
        val request = Request.Builder()
            .url(url)
            .get()
            .build()

        val response = client.newCall(request).execute()
        return response.body?.string() ?: throw Exception("Empty response body from $url")
    }

    /**
     * Fetch URL and follow redirects, return final URL.
     */
    fun resolveRedirect(url: String): String {
        val request = Request.Builder()
            .url(url)
            .get()
            .build()

        val response = client.newCall(request).execute()
        return response.request.url.toString()
    }

    companion object {
        const val BASE_URL = "https://anichin.moe"
        const val USER_AGENT = "Mozilla/5.0 (Linux; Android 14; Pixel 8) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/131.0.0.0 Mobile Safari/537.36"
    }
}

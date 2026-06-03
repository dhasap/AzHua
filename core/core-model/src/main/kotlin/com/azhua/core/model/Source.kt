package com.azhua.core.model

/**
 * Domain model for a content source (extension instance).
 */
data class Source(
    val id: String,
    val name: String,
    val lang: String,
    val baseUrl: String,
    val iconUrl: String = "",
)

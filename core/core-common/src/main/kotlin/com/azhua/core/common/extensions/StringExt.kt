package com.azhua.core.common.extensions

/**
 * Truncate string to max length with ellipsis.
 */
fun String.truncate(maxLength: Int): String {
    return if (length > maxLength) {
        take(maxLength - 3) + "..."
    } else {
        this
    }
}

/**
 * Parse episode number from string like "Episode 12" or "Ep. 12.5".
 */
fun String.parseEpisodeNumber(): Float {
    val regex = Regex("(?:episode|ep\.?\s*)(\d+(?:\.\d+)?)", RegexOption.IGNORE_CASE)
    return regex.find(this)?.groupValues?.get(1)?.toFloatOrNull() ?: 0f
}

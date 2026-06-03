package com.azhua.core.model

/**
 * Domain model for a Donghua (Chinese animation) entry.
 * Pure Kotlin, no Android dependencies.
 */
data class Donghua(
    val id: Long = 0,
    val sourceId: String,
    val sourceUrl: String,
    val title: String,
    val titleAlt: String? = null,
    val coverUrl: String? = null,
    val synopsis: String? = null,
    val genres: List<String> = emptyList(),
    val status: DonghuaStatus = DonghuaStatus.UNKNOWN,
    val studio: String? = null,
    val year: Int? = null,
    val rating: Float = 0f,
    val totalEpisodes: Int = 0,
    val isInLibrary: Boolean = false,
    val favoriteOrder: Int = 0,
    val dateAdded: Long = System.currentTimeMillis(),
    val lastUpdated: Long = System.currentTimeMillis(),
)

enum class DonghuaStatus {
    ONGOING, COMPLETED, HIATUS, CANCELLED, UNKNOWN;

    companion object {
        fun fromString(value: String): DonghuaStatus = when (value.uppercase()) {
            "ONGOING" -> ONGOING
            "COMPLETED" -> COMPLETED
            "HIATUS" -> HIATUS
            "CANCELLED" -> CANCELLED
            else -> UNKNOWN
        }
    }
}

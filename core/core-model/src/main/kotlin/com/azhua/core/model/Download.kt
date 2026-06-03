package com.azhua.core.model

/**
 * Domain model for a download entry.
 */
data class Download(
    val id: Long = 0,
    val episodeId: Long,
    val status: DownloadStatus = DownloadStatus.QUEUED,
    val progress: Float = 0f,
    val filePath: String? = null,
    val fileSize: Long = 0,
    val errorMsg: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
)

enum class DownloadStatus {
    QUEUED, DOWNLOADING, COMPLETED, ERROR
}

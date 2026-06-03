package com.azhua.core.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "download_table",
    foreignKeys = [
        ForeignKey(
            entity = EpisodeEntity::class,
            parentColumns = ["id"],
            childColumns = ["episode_id"],
            onDelete = ForeignKey.CASCADE,
        )
    ]
)
data class DownloadEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo(name = "episode_id")
    val episodeId: Long,
    val status: String = "QUEUED",
    val progress: Float = 0f,
    @ColumnInfo(name = "file_path")
    val filePath: String? = null,
    @ColumnInfo(name = "file_size")
    val fileSize: Long = 0,
    @ColumnInfo(name = "error_msg")
    val errorMsg: String? = null,
    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis(),
)

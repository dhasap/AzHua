package com.azhua.core.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "episode_table",
    foreignKeys = [
        ForeignKey(
            entity = DonghuaEntity::class,
            parentColumns = ["id"],
            childColumns = ["donghua_id"],
            onDelete = ForeignKey.CASCADE,
        )
    ],
    indices = [Index(value = ["donghua_id"])]
)
data class EpisodeEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo(name = "donghua_id")
    val donghuaId: Long,
    @ColumnInfo(name = "source_episode_id")
    val sourceEpisodeId: String,
    @ColumnInfo(name = "episode_number")
    val episodeNumber: Float,
    val title: String? = null,
    @ColumnInfo(name = "thumbnail_url")
    val thumbnailUrl: String? = null,
    @ColumnInfo(name = "duration_ms")
    val durationMs: Long = 0,
    @ColumnInfo(name = "date_upload")
    val dateUpload: Long = 0,
    @ColumnInfo(name = "last_watch_ms")
    val lastWatchMs: Long = 0,
    @ColumnInfo(name = "is_watched")
    val isWatched: Boolean = false,
    @ColumnInfo(name = "is_downloaded")
    val isDownloaded: Boolean = false,
    @ColumnInfo(name = "download_path")
    val downloadPath: String? = null,
)

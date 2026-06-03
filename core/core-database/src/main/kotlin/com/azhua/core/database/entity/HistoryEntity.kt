package com.azhua.core.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "history_table",
    foreignKeys = [
        ForeignKey(
            entity = DonghuaEntity::class,
            parentColumns = ["id"],
            childColumns = ["donghua_id"],
            onDelete = ForeignKey.CASCADE,
        ),
        ForeignKey(
            entity = EpisodeEntity::class,
            parentColumns = ["id"],
            childColumns = ["episode_id"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [
        Index(value = ["donghua_id"]),
        Index(value = ["episode_id"]),
        Index(value = ["watch_at"]),
    ]
)
data class HistoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo(name = "donghua_id")
    val donghuaId: Long,
    @ColumnInfo(name = "episode_id")
    val episodeId: Long,
    @ColumnInfo(name = "watch_at")
    val watchAt: Long,
    @ColumnInfo(name = "duration_ms")
    val durationMs: Long = 0,
)

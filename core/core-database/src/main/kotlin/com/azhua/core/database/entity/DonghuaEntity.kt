package com.azhua.core.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "donghua_table")
data class DonghuaEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo(name = "source_id")
    val sourceId: String,
    @ColumnInfo(name = "source_url")
    val sourceUrl: String,
    val title: String,
    @ColumnInfo(name = "title_alt")
    val titleAlt: String? = null,
    @ColumnInfo(name = "cover_url")
    val coverUrl: String? = null,
    val synopsis: String? = null,
    val genres: String = "[]", // JSON array
    val status: String = "UNKNOWN",
    val studio: String? = null,
    val year: Int? = null,
    val rating: Float = 0f,
    @ColumnInfo(name = "total_episodes")
    val totalEpisodes: Int = 0,
    @ColumnInfo(name = "in_library")
    val inLibrary: Boolean = false,
    @ColumnInfo(name = "favorite_order")
    val favoriteOrder: Int = 0,
    @ColumnInfo(name = "date_added")
    val dateAdded: Long = System.currentTimeMillis(),
    @ColumnInfo(name = "last_updated")
    val lastUpdated: Long = System.currentTimeMillis(),
)

package com.azhua.core.database.dao

import androidx.room.*
import com.azhua.core.database.entity.DownloadEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DownloadDao {
    @Query("SELECT * FROM download_table ORDER BY created_at DESC")
    fun getAllDownloads(): Flow<List<DownloadEntity>>

    @Query("SELECT * FROM download_table WHERE status = :status")
    fun getDownloadsByStatus(status: String): Flow<List<DownloadEntity>>

    @Query("SELECT * FROM download_table WHERE episode_id = :episodeId")
    suspend fun getDownloadByEpisodeId(episodeId: Long): DownloadEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(download: DownloadEntity): Long

    @Update
    suspend fun update(download: DownloadEntity)

    @Delete
    suspend fun delete(download: DownloadEntity)

    @Query("UPDATE download_table SET status = :status, progress = :progress WHERE id = :id")
    suspend fun updateStatus(id: Long, status: String, progress: Float)
}

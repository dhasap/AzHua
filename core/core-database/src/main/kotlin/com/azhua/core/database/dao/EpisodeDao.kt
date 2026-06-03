package com.azhua.core.database.dao

import androidx.room.*
import com.azhua.core.database.entity.EpisodeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface EpisodeDao {
    @Query("SELECT * FROM episode_table WHERE donghua_id = :donghuaId ORDER BY episode_number ASC")
    fun getEpisodesByDonghua(donghuaId: Long): Flow<List<EpisodeEntity>>

    @Query("SELECT * FROM episode_table WHERE id = :id")
    suspend fun getEpisodeById(id: Long): EpisodeEntity?

    @Query("SELECT * FROM episode_table WHERE id = :id")
    fun getEpisodeByIdFlow(id: Long): Flow<EpisodeEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(episode: EpisodeEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(episodes: List<EpisodeEntity>)

    @Query("UPDATE episode_table SET last_watch_ms = :positionMs, is_watched = :isWatched WHERE id = :episodeId")
    suspend fun updateWatchProgress(episodeId: Long, positionMs: Long, isWatched: Boolean)

    @Query("UPDATE episode_table SET is_downloaded = :isDownloaded, download_path = :path WHERE id = :episodeId")
    suspend fun updateDownloadStatus(episodeId: Long, isDownloaded: Boolean, path: String? = null)

    @Query("""
        SELECT e.* FROM episode_table e
        INNER JOIN donghua_table d ON e.donghua_id = d.id
        WHERE d.in_library = 1 AND e.is_watched = 0
        ORDER BY e.date_upload DESC
        LIMIT :limit
    """)
    fun getNewEpisodesInLibrary(limit: Int = 50): Flow<List<EpisodeEntity>>

    @Query("""
        SELECT e.* FROM episode_table e
        INNER JOIN donghua_table d ON e.donghua_id = d.id
        WHERE d.in_library = 1 AND e.last_watch_ms > 0 AND e.is_watched = 0
        ORDER BY e.last_watch_ms DESC
    """)
    fun getContinueWatching(): Flow<List<EpisodeEntity>>

    @Delete
    suspend fun delete(episode: EpisodeEntity)
}

package com.azhua.data.repository

import com.azhua.core.model.Episode
import kotlinx.coroutines.flow.Flow

interface EpisodeRepository {
    fun getEpisodesByDonghua(donghuaId: Long): Flow<List<Episode>>
    fun getContinueWatching(): Flow<List<Episode>>
    fun getNewEpisodes(): Flow<List<Episode>>
    suspend fun updateWatchProgress(episodeId: Long, positionMs: Long, isWatched: Boolean)
    suspend fun insertAll(episodes: List<Episode>)
}

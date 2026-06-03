package com.azhua.data.repository

import com.azhua.core.database.dao.EpisodeDao
import com.azhua.core.model.Episode
import com.azhua.data.mapper.toDomain
import com.azhua.data.mapper.toEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EpisodeRepositoryImpl @Inject constructor(
    private val episodeDao: EpisodeDao,
) : EpisodeRepository {

    override fun getEpisodesByDonghua(donghuaId: Long): Flow<List<Episode>> {
        return episodeDao.getEpisodesByDonghua(donghuaId).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getContinueWatching(): Flow<List<Episode>> {
        return episodeDao.getContinueWatching().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getNewEpisodes(): Flow<List<Episode>> {
        return episodeDao.getNewEpisodesInLibrary().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun updateWatchProgress(episodeId: Long, positionMs: Long, isWatched: Boolean) {
        episodeDao.updateWatchProgress(episodeId, positionMs, isWatched)
    }

    override suspend fun insertAll(episodes: List<Episode>) {
        episodeDao.insertAll(episodes.map { it.toEntity() })
    }
}

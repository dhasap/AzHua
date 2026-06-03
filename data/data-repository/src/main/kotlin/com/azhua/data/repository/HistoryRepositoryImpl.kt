package com.azhua.data.repository

import com.azhua.core.database.dao.HistoryDao
import com.azhua.core.database.entity.HistoryEntity
import com.azhua.core.model.History
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HistoryRepositoryImpl @Inject constructor(
    private val historyDao: HistoryDao,
) : HistoryRepository {

    override fun getRecentHistory(limit: Int): Flow<List<History>> {
        return historyDao.getRecentHistory(limit).map { entities ->
            entities.map {
                History(
                    id = it.id,
                    donghuaId = it.donghuaId,
                    episodeId = it.episodeId,
                    watchAt = it.watchAt,
                    durationMs = it.durationMs,
                )
            }
        }
    }

    override suspend fun insert(history: History): Long {
        return historyDao.insert(
            HistoryEntity(
                donghuaId = history.donghuaId,
                episodeId = history.episodeId,
                watchAt = history.watchAt,
                durationMs = history.durationMs,
            )
        )
    }

    override suspend fun delete(history: History) {
        historyDao.delete(
            HistoryEntity(
                id = history.id,
                donghuaId = history.donghuaId,
                episodeId = history.episodeId,
                watchAt = history.watchAt,
                durationMs = history.durationMs,
            )
        )
    }
}

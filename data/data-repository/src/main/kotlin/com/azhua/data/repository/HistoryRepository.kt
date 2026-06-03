package com.azhua.data.repository

import com.azhua.core.model.History
import kotlinx.coroutines.flow.Flow

interface HistoryRepository {
    fun getRecentHistory(limit: Int = 100): Flow<List<History>>
    suspend fun insert(history: History): Long
    suspend fun delete(history: History)
}

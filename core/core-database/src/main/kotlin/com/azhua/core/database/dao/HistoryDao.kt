package com.azhua.core.database.dao

import androidx.room.*
import com.azhua.core.database.entity.HistoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HistoryDao {
    @Query("SELECT * FROM history_table ORDER BY watch_at DESC LIMIT :limit")
    fun getRecentHistory(limit: Int = 100): Flow<List<HistoryEntity>>

    @Query("SELECT * FROM history_table WHERE watch_at >= :since ORDER BY watch_at DESC")
    fun getHistorySince(since: Long): Flow<List<HistoryEntity>>

    @Query("SELECT * FROM history_table WHERE donghua_id = :donghuaId ORDER BY watch_at DESC")
    fun getHistoryByDonghua(donghuaId: Long): Flow<List<HistoryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(history: HistoryEntity): Long

    @Delete
    suspend fun delete(history: HistoryEntity)

    @Query("DELETE FROM history_table WHERE watch_at < :before")
    suspend fun deleteOlderThan(before: Long)

    @Query("SELECT COUNT(*) FROM history_table WHERE watch_at >= :since")
    suspend fun getCountSince(since: Long): Int
}

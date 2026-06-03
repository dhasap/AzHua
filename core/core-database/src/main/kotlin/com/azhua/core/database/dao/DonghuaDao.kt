package com.azhua.core.database.dao

import androidx.room.*
import com.azhua.core.database.entity.DonghuaEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DonghuaDao {
    @Query("SELECT * FROM donghua_table WHERE in_library = 1 ORDER BY title ASC")
    fun getLibraryDonghua(): Flow<List<DonghuaEntity>>

    @Query("SELECT * FROM donghua_table WHERE in_library = 1 ORDER BY last_updated DESC")
    fun getLibraryByLastUpdated(): Flow<List<DonghuaEntity>>

    @Query("SELECT * FROM donghua_table WHERE id = :id")
    fun getDonghuaById(id: Long): Flow<DonghuaEntity?>

    @Query("SELECT * FROM donghua_table WHERE id = :id")
    suspend fun getDonghuaByIdOnce(id: Long): DonghuaEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(donghua: DonghuaEntity): Long

    @Query("UPDATE donghua_table SET in_library = :inLibrary WHERE id = :id")
    suspend fun updateLibraryStatus(id: Long, inLibrary: Boolean)

    @Query("UPDATE donghua_table SET last_updated = :timestamp WHERE id = :id")
    suspend fun updateLastUpdated(id: Long, timestamp: Long = System.currentTimeMillis())

    @Delete
    suspend fun delete(donghua: DonghuaEntity)

    @Query("DELETE FROM donghua_table WHERE id IN (:ids)")
    suspend fun deleteByIds(ids: List<Long>)

    @Query("SELECT * FROM donghua_table WHERE title LIKE '%' || :query || '%' AND in_library = 1")
    fun searchLibrary(query: String): Flow<List<DonghuaEntity>>
}

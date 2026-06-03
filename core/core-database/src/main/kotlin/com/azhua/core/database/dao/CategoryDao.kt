package com.azhua.core.database.dao

import androidx.room.*
import com.azhua.core.database.entity.CategoryEntity
import com.azhua.core.database.entity.DonghuaCategoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {
    @Query("SELECT * FROM category_table ORDER BY sort_order ASC")
    fun getAllCategories(): Flow<List<CategoryEntity>>

    @Query("SELECT * FROM category_table WHERE id = :id")
    suspend fun getCategoryById(id: Long): CategoryEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(category: CategoryEntity): Long

    @Update
    suspend fun update(category: CategoryEntity)

    @Delete
    suspend fun delete(category: CategoryEntity)

    @Query("SELECT * FROM donghua_category_table WHERE donghua_id = :donghuaId")
    suspend fun getCategoriesForDonghua(donghuaId: Long): List<DonghuaCategoryEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToCategory(crossRef: DonghuaCategoryEntity)

    @Delete
    suspend fun removeFromCategory(crossRef: DonghuaCategoryEntity)

    @Query("DELETE FROM donghua_category_table WHERE donghua_id = :donghuaId AND category_id = :categoryId")
    suspend fun removeFromCategory(donghuaId: Long, categoryId: Long)
}

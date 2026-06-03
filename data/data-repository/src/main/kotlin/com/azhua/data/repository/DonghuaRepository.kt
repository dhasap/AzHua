package com.azhua.data.repository

import com.azhua.core.model.AzResult
import com.azhua.core.model.Donghua
import kotlinx.coroutines.flow.Flow

interface DonghuaRepository {
    fun getLibraryDonghua(): Flow<List<Donghua>>
    fun getDonghuaById(id: Long): Flow<Donghua?>
    suspend fun insertOrUpdate(donghua: Donghua): Long
    suspend fun toggleLibraryStatus(id: Long, inLibrary: Boolean)
    suspend fun deleteDonghua(id: Long)
    fun searchLibrary(query: String): Flow<List<Donghua>>
}

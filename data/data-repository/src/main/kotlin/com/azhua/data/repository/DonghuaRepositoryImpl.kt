package com.azhua.data.repository

import com.azhua.core.database.dao.DonghuaDao
import com.azhua.core.model.Donghua
import com.azhua.data.mapper.toDomain
import com.azhua.data.mapper.toEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DonghuaRepositoryImpl @Inject constructor(
    private val donghuaDao: DonghuaDao,
) : DonghuaRepository {

    override fun getLibraryDonghua(): Flow<List<Donghua>> {
        return donghuaDao.getLibraryDonghua().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getDonghuaById(id: Long): Flow<Donghua?> {
        return donghuaDao.getDonghuaById(id).map { it?.toDomain() }
    }

    override suspend fun insertOrUpdate(donghua: Donghua): Long {
        return donghuaDao.insertOrUpdate(donghua.toEntity())
    }

    override suspend fun toggleLibraryStatus(id: Long, inLibrary: Boolean) {
        donghuaDao.updateLibraryStatus(id, inLibrary)
    }

    override suspend fun deleteDonghua(id: Long) {
        donghuaDao.getDonghuaByIdOnce(id)?.let { donghuaDao.delete(it) }
    }

    override fun searchLibrary(query: String): Flow<List<Donghua>> {
        return donghuaDao.searchLibrary(query).map { entities ->
            entities.map { it.toDomain() }
        }
    }
}

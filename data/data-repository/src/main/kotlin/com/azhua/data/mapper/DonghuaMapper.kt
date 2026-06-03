package com.azhua.data.mapper

import com.azhua.core.database.entity.DonghuaEntity
import com.azhua.core.model.Donghua
import com.azhua.core.model.DonghuaStatus
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

fun DonghuaEntity.toDomain(): Donghua {
    val genresType = object : TypeToken<List<String>>() {}.type
    val genresList: List<String> = try {
        Gson().fromJson(genres, genresType) ?: emptyList()
    } catch (e: Exception) {
        emptyList()
    }

    return Donghua(
        id = id,
        sourceId = sourceId,
        sourceUrl = sourceUrl,
        title = title,
        titleAlt = titleAlt,
        coverUrl = coverUrl,
        synopsis = synopsis,
        genres = genresList,
        status = DonghuaStatus.fromString(status),
        studio = studio,
        year = year,
        rating = rating,
        totalEpisodes = totalEpisodes,
        isInLibrary = inLibrary,
        favoriteOrder = favoriteOrder,
        dateAdded = dateAdded,
        lastUpdated = lastUpdated,
    )
}

fun Donghua.toEntity(): DonghuaEntity {
    return DonghuaEntity(
        id = id,
        sourceId = sourceId,
        sourceUrl = sourceUrl,
        title = title,
        titleAlt = titleAlt,
        coverUrl = coverUrl,
        synopsis = synopsis,
        genres = Gson().toJson(genres),
        status = status.name,
        studio = studio,
        year = year,
        rating = rating,
        totalEpisodes = totalEpisodes,
        inLibrary = isInLibrary,
        favoriteOrder = favoriteOrder,
        dateAdded = dateAdded,
        lastUpdated = lastUpdated,
    )
}

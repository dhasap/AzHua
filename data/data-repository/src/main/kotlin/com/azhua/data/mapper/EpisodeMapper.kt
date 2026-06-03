package com.azhua.data.mapper

import com.azhua.core.database.entity.EpisodeEntity
import com.azhua.core.model.Episode

fun EpisodeEntity.toDomain(): Episode {
    return Episode(
        id = id,
        donghuaId = donghuaId,
        sourceEpisodeId = sourceEpisodeId,
        episodeNumber = episodeNumber,
        title = title,
        thumbnailUrl = thumbnailUrl,
        durationMs = durationMs,
        dateUpload = dateUpload,
        lastWatchMs = lastWatchMs,
        isWatched = isWatched,
        isDownloaded = isDownloaded,
        downloadPath = downloadPath,
    )
}

fun Episode.toEntity(): EpisodeEntity {
    return EpisodeEntity(
        id = id,
        donghuaId = donghuaId,
        sourceEpisodeId = sourceEpisodeId,
        episodeNumber = episodeNumber,
        title = title,
        thumbnailUrl = thumbnailUrl,
        durationMs = durationMs,
        dateUpload = dateUpload,
        lastWatchMs = lastWatchMs,
        isWatched = isWatched,
        isDownloaded = isDownloaded,
        downloadPath = downloadPath,
    )
}

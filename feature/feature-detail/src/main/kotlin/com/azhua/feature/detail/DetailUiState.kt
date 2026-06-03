package com.azhua.feature.detail

import com.azhua.core.model.Category
import com.azhua.core.model.Donghua
import com.azhua.core.model.Episode

data class DetailUiState(
    val donghua: Donghua? = null,
    val episodes: List<Episode> = emptyList(),
    val isInLibrary: Boolean = false,
    val currentCategory: Category? = null,
    val watchProgress: Map<Long, Float> = emptyMap(),
    val isLoading: Boolean = true,
    val error: String? = null,
    val episodeSortOrder: EpisodeSortOrder = EpisodeSortOrder.NEWEST,
    val episodeFilter: EpisodeFilter = EpisodeFilter.ALL,
    val searchQuery: String = "",
)

enum class EpisodeSortOrder(val label: String) {
    NEWEST("Terbaru"),
    OLDEST("Terlama"),
}

enum class EpisodeFilter(val label: String) {
    ALL("Semua"),
    UNWATCHED("Belum Ditonton"),
    WATCHED("Sudah Ditonton"),
    DOWNLOADED("Didownload"),
}

sealed class DetailEvent {
    data object ToggleLibrary : DetailEvent()
    data class PlayEpisode(val episodeId: Long) : DetailEvent()
    data class SortChanged(val sort: EpisodeSortOrder) : DetailEvent()
    data class FilterChanged(val filter: EpisodeFilter) : DetailEvent()
    data class SearchChanged(val query: String) : DetailEvent()
    data object Retry : DetailEvent()
}

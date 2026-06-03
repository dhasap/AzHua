package com.azhua.feature.recents

import com.azhua.core.model.DonghuaWithProgress
import com.azhua.core.model.Episode
import java.time.LocalDate

sealed class RecentsUiState {
    data object Loading : RecentsUiState()
    data object Empty : RecentsUiState()
    data class Error(val message: String) : RecentsUiState()
    data class Success(
        val continueWatching: List<DonghuaWithProgress>,
        val newEpisodes: List<EpisodeUpdate>,
        val history: List<HistoryDay>,
        val activeTab: RecentsTab,
    ) : RecentsUiState()
}

enum class RecentsTab(val label: String) {
    ALL("Semua"),
    CONTINUE("Lanjutkan"),
    NEW_EPISODES("Episode Baru"),
}

data class EpisodeUpdate(
    val donghuaId: Long,
    val donghuaTitle: String,
    val coverUrl: String?,
    val episodeNumbers: List<Float>,
    val timestamp: Long,
)

data class HistoryDay(
    val date: LocalDate,
    val items: List<HistoryItem>,
)

data class HistoryItem(
    val donghuaId: Long,
    val donghuaTitle: String,
    val coverUrl: String?,
    val episodeNumber: Float,
    val episodeTitle: String?,
    val watchAt: Long,
    val durationMs: Long,
    val progress: Float,
)

sealed class RecentsEvent {
    data class TabChanged(val tab: RecentsTab) : RecentsEvent()
    data class ResumeWatching(val donghuaId: Long, val episodeId: Long) : RecentsEvent()
    data class DeleteHistory(val historyItem: HistoryItem) : RecentsEvent()
    data object Retry : RecentsEvent()
}

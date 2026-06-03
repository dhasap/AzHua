package com.azhua.feature.recents

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.azhua.data.repository.DonghuaRepository
import com.azhua.data.repository.EpisodeRepository
import com.azhua.data.repository.HistoryRepository
import com.azhua.core.model.DonghuaWithProgress
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecentsViewModel @Inject constructor(
    private val donghuaRepository: DonghuaRepository,
    private val episodeRepository: EpisodeRepository,
    private val historyRepository: HistoryRepository,
) : ViewModel() {

    private val _activeTab = MutableStateFlow(RecentsTab.ALL)

    val uiState: StateFlow<RecentsUiState> = combine(
        donghuaRepository.getLibraryDonghua(),
        episodeRepository.getContinueWatching(),
        episodeRepository.getNewEpisodes(),
        historyRepository.getRecentHistory(),
        _activeTab,
    ) { donghuaList, continueWatchingEpisodes, newEpisodeList, historyList, tab ->
        // Build continue watching
        val continueWatching = continueWatchingEpisodes.mapNotNull { ep ->
            val donghua = donghuaList.find { it.id == ep.donghuaId }
            donghua?.let {
                DonghuaWithProgress(
                    donghua = it,
                    lastEpisode = ep,
                    progress = ep.watchProgress,
                    lastWatchedAt = ep.lastWatchMs,
                )
            }
        }.sortedByDescending { it.lastWatchedAt }

        // Build new episodes grouped by donghua
        val newEpisodes = newEpisodeList
            .groupBy { it.donghuaId }
            .map { (donghuaId, episodes) ->
                val donghua = donghuaList.find { it.id == donghuaId }
                EpisodeUpdate(
                    donghuaId = donghuaId,
                    donghuaTitle = donghua?.title ?: "Unknown",
                    coverUrl = donghua?.coverUrl,
                    episodeNumbers = episodes.map { it.episodeNumber }.sorted(),
                    timestamp = episodes.maxOfOrNull { it.dateUpload } ?: 0L,
                )
            }
            .sortedByDescending { it.timestamp }

        // Build history grouped by day
        val historyItems = historyList.map { h ->
            val donghua = donghuaList.find { it.id == h.donghuaId }
            val episode = continueWatchingEpisodes.find { it.id == h.episodeId }
            HistoryItem(
                donghuaId = h.donghuaId,
                donghuaTitle = donghua?.title ?: "Unknown",
                coverUrl = donghua?.coverUrl,
                episodeNumber = episode?.episodeNumber ?: 0f,
                episodeTitle = episode?.title,
                watchAt = h.watchAt,
                durationMs = h.durationMs,
                progress = episode?.watchProgress ?: 0f,
            )
        }

        val historyDays = historyItems
            .groupBy {
                java.time.Instant.ofEpochMilli(it.watchAt)
                    .atZone(java.time.ZoneId.systemDefault())
                    .toLocalDate()
            }
            .map { (date, items) -> HistoryDay(date = date, items = items.sortedByDescending { it.watchAt }) }
            .sortedByDescending { it.date }

        if (continueWatching.isEmpty() && newEpisodes.isEmpty() && historyDays.isEmpty()) {
            RecentsUiState.Empty
        } else {
            RecentsUiState.Success(
                continueWatching = continueWatching,
                newEpisodes = newEpisodes,
                history = historyDays,
                activeTab = tab,
            )
        }
    }.catch { e ->
        emit(RecentsUiState.Error(e.message ?: "Unknown error"))
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = RecentsUiState.Loading,
    )

    fun onEvent(event: RecentsEvent) {
        when (event) {
            is RecentsEvent.TabChanged -> _activeTab.value = event.tab
            is RecentsEvent.ResumeWatching -> {
                // TODO: Launch player
            }
            is RecentsEvent.DeleteHistory -> {
                // TODO: Delete from history
            }
            RecentsEvent.Retry -> {
                // Flow will auto-retry
            }
        }
    }
}

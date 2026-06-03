package com.azhua.feature.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.azhua.data.repository.DonghuaRepository
import com.azhua.data.repository.EpisodeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val donghuaRepository: DonghuaRepository,
    private val episodeRepository: EpisodeRepository,
) : ViewModel() {

    private val donghuaId: Long = savedStateHandle["donghuaId"] ?: 0L
    private val _uiState = MutableStateFlow(DetailUiState(isLoading = true))
    val uiState: StateFlow<DetailUiState> = _uiState.asStateFlow()

    init {
        loadDetail()
    }

    private fun loadDetail() {
        viewModelScope.launch {
            donghuaRepository.getDonghuaById(donghuaId).collect { donghua ->
                if (donghua != null) {
                    _uiState.update { it.copy(donghua = donghua, isInLibrary = donghua.isInLibrary) }
                } else {
                    _uiState.update { it.copy(error = "Donghua tidak ditemukan", isLoading = false) }
                }
            }
        }
        viewModelScope.launch {
            episodeRepository.getEpisodesByDonghua(donghuaId).collect { episodes ->
                _uiState.update { it.copy(episodes = episodes, isLoading = false) }
            }
        }
    }

    fun onEvent(event: DetailEvent) {
        when (event) {
            DetailEvent.ToggleLibrary -> {
                val current = _uiState.value
                viewModelScope.launch {
                    donghuaRepository.toggleLibraryStatus(donghuaId, !current.isInLibrary)
                }
            }
            is DetailEvent.PlayEpisode -> {
                // TODO: Launch player
            }
            is DetailEvent.SortChanged -> {
                _uiState.update { it.copy(episodeSortOrder = event.sort) }
            }
            is DetailEvent.FilterChanged -> {
                _uiState.update { it.copy(episodeFilter = event.filter) }
            }
            is DetailEvent.SearchChanged -> {
                _uiState.update { it.copy(searchQuery = event.query) }
            }
            DetailEvent.Retry -> {
                _uiState.update { it.copy(error = null, isLoading = true) }
                loadDetail()
            }
        }
    }
}

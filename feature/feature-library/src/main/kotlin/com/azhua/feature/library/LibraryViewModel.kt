package com.azhua.feature.library

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.azhua.core.model.CategoryWithDonghua
import com.azhua.core.model.DonghuaWithProgress
import com.azhua.data.repository.DonghuaRepository
import com.azhua.data.repository.EpisodeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LibraryViewModel @Inject constructor(
    private val donghuaRepository: DonghuaRepository,
    private val episodeRepository: EpisodeRepository,
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    private val _isSearchActive = MutableStateFlow(false)
    private val _expandedCategoryIds = MutableStateFlow(setOf<Long>(1L)) // Default expand first
    private val _selectedDonghuaIds = MutableStateFlow(setOf<Long>())
    private val _isMultiSelectMode = MutableStateFlow(false)
    private val _filter = MutableStateFlow(LibraryFilter())
    private val _gridColumns = MutableStateFlow(2)
    private val _error = MutableStateFlow<String?>(null)

    val uiState: StateFlow<LibraryUiState> = combine(
        donghuaRepository.getLibraryDonghua(),
        episodeRepository.getContinueWatching(),
        _searchQuery,
        _isSearchActive,
        _expandedCategoryIds,
        _selectedDonghuaIds,
        _isMultiSelectMode,
        _filter,
        _gridColumns,
        _error,
    ) { values ->
        @Suppress("UNCHECKED_CAST")
        val donghuaList = values[0] as List<com.azhua.core.model.Donghua>
        val continueWatchingEpisodes = values[1] as List<com.azhua.core.model.Episode>
        val searchQuery = values[2] as String
        val isSearchActive = values[3] as Boolean
        val expandedIds = values[4] as Set<Long>
        val selectedIds = values[5] as Set<Long>
        val isMultiSelect = values[6] as Boolean
        val filter = values[7] as LibraryFilter
        val columns = values[8] as Int
        val error = values[9] as String?

        if (error != null) {
            return@combine LibraryUiState.Error(error)
        }

        if (donghuaList.isEmpty()) {
            return@combine LibraryUiState.Empty
        }

        // Filter by search query
        val filtered = if (searchQuery.isBlank()) donghuaList
        else donghuaList.filter { it.title.contains(searchQuery, ignoreCase = true) }

        // Filter by status
        val statusFiltered = when (filter.statusFilter) {
            StatusFilter.ALL -> filtered
            else -> filtered.filter { it.status.name == filter.statusFilter.name }
        }

        // Sort
        val sorted = when (filter.sortBy) {
            SortOption.TITLE_ASC -> statusFiltered.sortedBy { it.title.lowercase() }
            SortOption.TITLE_DESC -> statusFiltered.sortedByDescending { it.title.lowercase() }
            SortOption.LAST_WATCHED -> statusFiltered.sortedByDescending { it.lastUpdated }
            SortOption.LAST_ADDED -> statusFiltered.sortedByDescending { it.dateAdded }
            SortOption.LAST_UPDATED -> statusFiltered.sortedByDescending { it.lastUpdated }
        }

        // Group into single category for now (TODO: real categories)
        val categories = listOf(
            CategoryWithDonghua(
                category = com.azhua.core.model.Category(id = 1, name = "Favorit", isDefault = true),
                donghuaList = sorted,
            )
        )

        // Continue watching
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

        LibraryUiState.Success(
            categories = categories,
            expandedCategoryIds = expandedIds,
            continueWatching = continueWatching,
            activeFilter = filter,
            searchQuery = searchQuery,
            isSearchActive = isSearchActive,
            selectedDonghuaIds = selectedIds,
            isMultiSelectMode = isMultiSelect,
            gridColumns = columns,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = LibraryUiState.Loading,
    )

    fun onEvent(event: LibraryEvent) {
        when (event) {
            is LibraryEvent.ToggleCategory -> {
                _expandedCategoryIds.update { ids ->
                    if (event.categoryId in ids) ids - event.categoryId
                    else ids + event.categoryId
                }
            }
            is LibraryEvent.SearchQueryChanged -> {
                _searchQuery.value = event.query
            }
            LibraryEvent.ToggleSearch -> {
                _isSearchActive.update { !it }
                if (!_isSearchActive.value) _searchQuery.value = ""
            }
            is LibraryEvent.SortChanged -> {
                _filter.update { it.copy(sortBy = event.sort) }
            }
            is LibraryEvent.StatusFilterChanged -> {
                _filter.update { it.copy(statusFilter = event.filter) }
            }
            is LibraryEvent.DonghuaSelected -> {
                if (_isMultiSelectMode.value) {
                    _selectedDonghuaIds.update { ids ->
                        if (event.donghuaId in ids) ids - event.donghuaId
                        else ids + event.donghuaId
                    }
                }
            }
            LibraryEvent.ClearSelection -> {
                _selectedDonghuaIds.value = emptySet()
                _isMultiSelectMode.value = false
            }
            LibraryEvent.SelectAll -> {
                val current = uiState.value
                if (current is LibraryUiState.Success) {
                    val allIds = current.categories.flatMap { cat -> cat.donghuaList.map { it.id } }.toSet()
                    _selectedDonghuaIds.value = allIds
                }
            }
            is LibraryEvent.RemoveFromLibrary -> {
                viewModelScope.launch {
                    event.donghuaIds.forEach { id ->
                        donghuaRepository.toggleLibraryStatus(id, false)
                    }
                    _selectedDonghuaIds.value = emptySet()
                    _isMultiSelectMode.value = false
                }
            }
            is LibraryEvent.ToggleGridColumns -> {
                _gridColumns.value = event.columns
            }
            LibraryEvent.Retry -> {
                _error.value = null
            }
        }
    }

    fun enableMultiSelect(donghuaId: Long) {
        _isMultiSelectMode.value = true
        _selectedDonghuaIds.value = setOf(donghuaId)
    }
}

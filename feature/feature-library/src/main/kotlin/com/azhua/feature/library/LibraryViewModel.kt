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
    private val _expandedCategoryIds = MutableStateFlow(setOf<Long>(1L))
    private val _selectedDonghuaIds = MutableStateFlow(setOf<Long>())
    private val _isMultiSelectMode = MutableStateFlow(false)
    private val _filter = MutableStateFlow(LibraryFilter())
    private val _gridColumns = MutableStateFlow(2)

    // Combine in smaller groups to avoid fragile positional indexing
    private val dataFlow = combine(
        donghuaRepository.getLibraryDonghua(),
        episodeRepository.getContinueWatching(),
    ) { donghuaList, continueWatchingEpisodes ->
        Pair(donghuaList, continueWatchingEpisodes)
    }

    private val uiConfigFlow = combine(
        _searchQuery,
        _isSearchActive,
        _expandedCategoryIds,
        _selectedDonghuaIds,
        _isMultiSelectMode,
        _filter,
        _gridColumns,
    ) { searchQuery, isSearchActive, expandedIds, selectedIds, isMultiSelect, filter, columns ->
        UiConfig(searchQuery, isSearchActive, expandedIds, selectedIds, isMultiSelect, filter, columns)
    }

    val uiState: StateFlow<LibraryUiState> = combine(
        dataFlow,
        uiConfigFlow,
    ) { (donghuaList, continueWatchingEpisodes), config ->
        if (donghuaList.isEmpty()) {
            return@combine LibraryUiState.Empty
        }

        // Filter by search query
        val filtered = if (config.searchQuery.isBlank()) donghuaList
        else donghuaList.filter { it.title.contains(config.searchQuery, ignoreCase = true) }

        // Filter by status
        val statusFiltered = when (config.filter.statusFilter) {
            StatusFilter.ALL -> filtered
            else -> filtered.filter { it.status.name == config.filter.statusFilter.name }
        }

        // Sort
        val sorted = when (config.filter.sortBy) {
            SortOption.TITLE_ASC -> statusFiltered.sortedBy { it.title.lowercase() }
            SortOption.TITLE_DESC -> statusFiltered.sortedByDescending { it.title.lowercase() }
            SortOption.LAST_WATCHED -> statusFiltered.sortedByDescending { it.lastUpdated }
            SortOption.LAST_ADDED -> statusFiltered.sortedByDescending { it.dateAdded }
            SortOption.LAST_UPDATED -> statusFiltered.sortedByDescending { it.lastUpdated }
        }

        // Group into categories
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
            expandedCategoryIds = config.expandedCategoryIds,
            continueWatching = continueWatching,
            activeFilter = config.filter,
            searchQuery = config.searchQuery,
            isSearchActive = config.isSearchActive,
            selectedDonghuaIds = config.selectedDonghuaIds,
            isMultiSelectMode = config.isMultiSelectMode,
            gridColumns = config.gridColumns,
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
                // Data flow auto-retries
            }
        }
    }

    fun enableMultiSelect(donghuaId: Long) {
        _isMultiSelectMode.value = true
        _selectedDonghuaIds.value = setOf(donghuaId)
    }
}

private data class UiConfig(
    val searchQuery: String,
    val isSearchActive: Boolean,
    val expandedCategoryIds: Set<Long>,
    val selectedDonghuaIds: Set<Long>,
    val isMultiSelectMode: Boolean,
    val filter: LibraryFilter,
    val gridColumns: Int,
)

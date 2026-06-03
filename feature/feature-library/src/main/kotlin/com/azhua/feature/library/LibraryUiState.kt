package com.azhua.feature.library

import com.azhua.core.model.Category
import com.azhua.core.model.CategoryWithDonghua
import com.azhua.core.model.DonghuaWithProgress

sealed class LibraryUiState {
    data object Loading : LibraryUiState()
    data object Empty : LibraryUiState()
    data class Error(val message: String) : LibraryUiState()
    data class Success(
        val categories: List<CategoryWithDonghua>,
        val expandedCategoryIds: Set<Long>,
        val continueWatching: List<DonghuaWithProgress>,
        val activeFilter: LibraryFilter,
        val searchQuery: String,
        val isSearchActive: Boolean,
        val selectedDonghuaIds: Set<Long>,
        val isMultiSelectMode: Boolean,
        val gridColumns: Int,
    ) : LibraryUiState()
}

data class LibraryFilter(
    val sortBy: SortOption = SortOption.TITLE_ASC,
    val statusFilter: StatusFilter = StatusFilter.ALL,
)

enum class SortOption(val label: String) {
    TITLE_ASC("A-Z"),
    TITLE_DESC("Z-A"),
    LAST_WATCHED("Terakhir Ditonton"),
    LAST_ADDED("Terakhir Ditambah"),
    LAST_UPDATED("Terakhir Diperbarui"),
}

enum class StatusFilter(val label: String) {
    ALL("Semua"),
    ONGOING("Sedang Tayang"),
    COMPLETED("Selesai"),
    HIATUS("Hiatus"),
}

sealed class LibraryEvent {
    data class ToggleCategory(val categoryId: Long) : LibraryEvent()
    data class SearchQueryChanged(val query: String) : LibraryEvent()
    data object ToggleSearch : LibraryEvent()
    data class SortChanged(val sort: SortOption) : LibraryEvent()
    data class StatusFilterChanged(val filter: StatusFilter) : LibraryEvent()
    data class DonghuaSelected(val donghuaId: Long) : LibraryEvent()
    data object ClearSelection : LibraryEvent()
    data object SelectAll : LibraryEvent()
    data class RemoveFromLibrary(val donghuaIds: Set<Long>) : LibraryEvent()
    data class ToggleGridColumns(val columns: Int) : LibraryEvent()
    data object Retry : LibraryEvent()
}

package com.azhua.feature.settings

data class SettingsUiState(
    val theme: AppTheme = AppTheme.AMOLED,
    val gridColumns: Int = 2,
    val textSize: Float = 1f,
    val defaultQuality: String = "720p",
    val autoPlayNext: Boolean = true,
    val skipIntroDuration: Int = 10,
    val updateOnOpen: Boolean = true,
    val notifyNewEpisodes: Boolean = true,
    val totalDonghua: Int = 0,
    val totalEpisodes: Int = 0,
    val totalWatchTimeMs: Long = 0,
)

enum class AppTheme(val label: String) {
    DARK("Dark"),
    AMOLED("AMOLED"),
}

sealed class SettingsEvent {
    data class ThemeChanged(val theme: AppTheme) : SettingsEvent()
    data class GridColumnsChanged(val columns: Int) : SettingsEvent()
    data class TextSizeChanged(val size: Float) : SettingsEvent()
    data class DefaultQualityChanged(val quality: String) : SettingsEvent()
    data class AutoPlayNextChanged(val enabled: Boolean) : SettingsEvent()
    data class SkipIntroDurationChanged(val duration: Int) : SettingsEvent()
    data class UpdateOnOpenChanged(val enabled: Boolean) : SettingsEvent()
    data class NotifyNewEpisodesChanged(val enabled: Boolean) : SettingsEvent()
    data object ClearCache : SettingsEvent()
    data object CreateBackup : SettingsEvent()
    data object RestoreBackup : SettingsEvent()
}

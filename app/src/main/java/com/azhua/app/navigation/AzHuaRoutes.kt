package com.azhua.app.navigation

/**
 * Route constants for Compose Navigation.
 */
object AzHuaRoutes {
    // Bottom Nav
    const val LIBRARY = "library"
    const val DISCOVER = "discover"
    const val RECENTS = "recents"
    const val EXTENSIONS = "extensions"

    // Content
    const val DETAIL = "detail/{donghuaId}"
    const val BROWSE_SOURCE = "browse/{sourceId}"

    // Settings
    const val SETTINGS = "settings"
    const val STATS = "settings/stats"
    const val BACKUP = "settings/backup"
    const val DOWNLOADS = "settings/downloads"

    // Extension
    const val EXTENSION_SETTINGS = "extension/{pkgName}/settings"

    // Builder functions
    fun detail(donghuaId: Long) = "detail/$donghuaId"
    fun browseSource(sourceId: String) = "browse/$sourceId"
    fun extensionSettings(pkgName: String) = "extension/$pkgName/settings"
}

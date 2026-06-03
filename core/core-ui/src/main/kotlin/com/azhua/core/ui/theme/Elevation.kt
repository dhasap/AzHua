package com.azhua.core.ui.theme

import androidx.compose.ui.graphics.Color

/**
 * Color-based elevation system for OLED-friendly dark theme.
 * Instead of shadows, we use lighter surface colors.
 */
object AzHuaElevation {
    val Level0 = ColorBackground        // Screen background
    val Level1 = ColorSurface            // Bottom nav, card default
    val Level2 = ColorSurfaceVariant     // Card hover, selected
    val Level3 = ColorSurfaceContainer   // Dialog, bottom sheet
    val Level4 = ColorSurfaceContainerHigh // Elevated dialog
}

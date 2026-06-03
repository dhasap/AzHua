package com.azhua.core.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

val AzHuaDarkColorScheme = darkColorScheme(
    background = ColorBackground,
    surface = ColorSurface,
    surfaceVariant = ColorSurfaceVariant,
    surfaceContainer = ColorSurfaceContainer,
    surfaceContainerHigh = ColorSurfaceContainerHigh,
    primary = ColorPrimary,
    onPrimary = ColorOnPrimary,
    primaryContainer = ColorPrimaryContainer,
    onPrimaryContainer = ColorOnPrimaryContainer,
    secondary = ColorSecondary,
    onSecondary = ColorOnSecondary,
    secondaryContainer = ColorSecondaryContainer,
    onSecondaryContainer = ColorOnSecondaryContainer,
    tertiary = ColorTertiary,
    tertiaryContainer = ColorTertiaryContainer,
    onTertiaryContainer = ColorOnTertiaryContainer,
    error = ColorError,
    onError = ColorOnError,
    onBackground = ColorTextPrimary,
    onSurface = ColorTextPrimary,
    onSurfaceVariant = ColorTextSecondary,
    outline = ColorOutline,
    outlineVariant = ColorOutlineVariant,
)

@Composable
fun AzHuaTheme(
    content: @Composable () -> Unit
) {
    // Always dark theme — no light mode
    MaterialTheme(
        colorScheme = AzHuaDarkColorScheme,
        typography = AzHuaTypography,
        shapes = AzHuaShapes,
    ) {
        CompositionLocalProvider(
            LocalAzHuaSpacing provides AzHuaSpacing(),
            LocalAzHuaMotion provides AzHuaMotion,
        ) {
            content()
        }
    }
}

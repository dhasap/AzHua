package com.azhua.core.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
data class AzHuaSpacing(
    val XXS: Dp = 2.dp,
    val XS: Dp = 4.dp,
    val SM: Dp = 8.dp,
    val MD: Dp = 12.dp,
    val LG: Dp = 16.dp,
    val XL: Dp = 20.dp,
    val XXL: Dp = 24.dp,
    val S3XL: Dp = 32.dp,
    val S4XL: Dp = 40.dp,
    val S5XL: Dp = 48.dp,
)

val LocalAzHuaSpacing = staticCompositionLocalOf { AzHuaSpacing() }

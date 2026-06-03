package com.azhua.core.ui.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun ErrorState(
    message: String,
    modifier: Modifier = Modifier,
    icon: ImageVector = Icons.Outlined.ErrorOutline,
    onRetry: (() -> Unit)? = null,
) {
    EmptyState(
        icon = icon,
        title = "Terjadi Kesalahan",
        description = message,
        modifier = modifier,
        actionLabel = if (onRetry != null) "Coba Lagi" else null,
        onActionClick = onRetry,
    )
}

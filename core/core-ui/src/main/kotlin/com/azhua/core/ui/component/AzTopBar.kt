package com.azhua.core.ui.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.azhua.core.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AzTopBar(
    title: String,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    showBack: Boolean = false,
    onBackClick: (() -> Unit)? = null,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    actions: @Composable RowScope.() -> Unit = {},
) {
    val containerColor by animateColorAsState(
        targetValue = if (scrollBehavior?.state?.overlappedFraction ?: 0f > 0f) {
            ColorSurface
        } else {
            ColorBackground
        },
        label = "topbar_bg",
    )

    TopAppBar(
        title = {
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineSmall,
                    color = ColorTextPrimary,
                )
                subtitle?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodySmall,
                        color = ColorTextSecondary,
                    )
                }
            }
        },
        modifier = modifier,
        navigationIcon = {
            if (showBack) {
                IconButton(onClick = { onBackClick?.invoke() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Kembali",
                        tint = ColorIconDefault,
                    )
                }
            }
        },
        actions = actions,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = containerColor,
            scrolledContainerColor = ColorSurface,
        ),
        scrollBehavior = scrollBehavior,
    )
}

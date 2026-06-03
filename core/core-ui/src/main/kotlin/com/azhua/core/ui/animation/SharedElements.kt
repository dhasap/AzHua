package com.azhua.core.ui.animation

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import coil3.compose.AsyncImage

/**
 * Shared element transition for cover art images.
 * Used when navigating from grid to detail screen.
 */
@Composable
fun SharedCoverImage(
    imageUrl: String?,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop,
    animate: Boolean = true,
) {
    var scale by remember { mutableFloatStateOf(1f) }

    LaunchedEffect(animate) {
        if (animate) {
            scale = 0.8f
            animate(
                initialValue = 0.8f,
                targetValue = 1f,
                animationSpec = tween(400, easing = FastOutSlowInEasing),
            ) { value, _ ->
                scale = value
            }
        }
    }

    AsyncImage(
        model = imageUrl,
        contentDescription = contentDescription,
        modifier = modifier.graphicsLayer {
            scaleX = scale
            scaleY = scale
        },
        contentScale = contentScale,
    )
}

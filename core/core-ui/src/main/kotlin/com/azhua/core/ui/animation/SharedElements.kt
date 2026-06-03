package com.azhua.core.ui.animation

import androidx.compose.animation.core.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import coil3.compose.AsyncImage

/**
 * Animated cover image with scale-in effect.
 */
@Composable
fun SharedCoverImage(
    imageUrl: String?,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop,
    animate: Boolean = true,
) {
    val scale = remember { Animatable(0.8f) }

    LaunchedEffect(imageUrl) {
        if (animate) {
            scale.snapTo(0.8f)
            scale.animateTo(
                targetValue = 1f,
                animationSpec = tween(400, easing = FastOutSlowInEasing),
            )
        } else {
            scale.snapTo(1f)
        }
    }

    AsyncImage(
        model = imageUrl,
        contentDescription = contentDescription,
        modifier = modifier.graphicsLayer {
            scaleX = scale.value
            scaleY = scale.value
        },
        contentScale = contentScale,
    )
}

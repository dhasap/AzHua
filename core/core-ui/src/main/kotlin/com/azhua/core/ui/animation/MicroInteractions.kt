package com.azhua.core.ui.animation

import androidx.compose.animation.core.*
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

/**
 * Scale-down effect on press (like TachiyomiJ2K cards).
 */
fun Modifier.pressScaleEffect(
    pressedScale: Float = 0.97f,
    animationSpec: AnimationSpec<Float> = tween(100),
): Modifier = composed {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) pressedScale else 1f,
        animationSpec = animationSpec,
        label = "press_scale",
    )

    this
        .graphicsLayer {
            scaleX = scale
            scaleY = scale
        }
        .pointerInput(isPressed) {
            coroutineScope {
                launch {
                    while (true) {
                        awaitPointerEventScope {
                            awaitFirstDown(requireUnconsumed = false)
                            isPressed = true
                            waitForUpOrCancellation()
                            isPressed = false
                        }
                    }
                }
            }
        }
}

/**
 * Fade-in animation for screen content.
 */
fun Modifier.fadeInOnLoad(
    durationMs: Int = 300,
): Modifier = composed {
    var isVisible by remember { mutableStateOf(false) }
    val alpha by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec = tween(durationMs),
        label = "fade_in",
    )

    LaunchedEffect(Unit) {
        isVisible = true
    }

    this.graphicsLayer {
        this.alpha = alpha
    }
}

/**
 * Slide-up animation for screen content.
 */
fun Modifier.slideUpOnLoad(
    durationMs: Int = 400,
    distancePx: Float = 50f,
): Modifier = composed {
    var isVisible by remember { mutableStateOf(false) }
    val offsetY by animateFloatAsState(
        targetValue = if (isVisible) 0f else distancePx,
        animationSpec = tween(durationMs, easing = FastOutSlowInEasing),
        label = "slide_up",
    )
    val alpha by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec = tween(durationMs),
        label = "fade_in_slide",
    )

    LaunchedEffect(Unit) {
        isVisible = true
    }

    this.graphicsLayer {
        translationY = offsetY
        this.alpha = alpha
    }
}

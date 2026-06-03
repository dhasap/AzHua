package com.azhua.core.ui.animation

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import com.azhua.core.ui.theme.AzHuaMotion

/**
 * Standard screen enter/exit transitions.
 */
object AzHuaTransitions {
    val screenEnter = fadeIn(tween(300)) + slideInVertically(tween(300)) { it / 10 }
    val screenExit = fadeOut(tween(200))
    val screenPopEnter = fadeIn(tween(300))
    val screenPopExit = fadeOut(tween(200)) + slideOutVertically(tween(200)) { it / 10 }

    /**
     * Card press animation spec.
     */
    val cardPress = tween<Float>(100, easing = AzHuaMotion.Standard)

    /**
     * Badge appear animation spec.
     */
    val badgeAppear = keyframes {
        durationMillis = 300
        0f at 0
        1.2f at 150
        1f at 300
    }

    /**
     * Tab icon scale animation spec.
     */
    val tabIconScale = keyframes {
        durationMillis = 200
        1f at 0
        1.15f at 100
        1f at 200
    }
}

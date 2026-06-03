package com.azhua.core.ui.theme

import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf

@Immutable
object AzHuaMotion {
    // Easing functions
    val Standard = CubicBezierEasing(0.2f, 0f, 0f, 1f)
    val Emphasized = CubicBezierEasing(0.2f, 0f, 0f, 1f)
    val EmphasizedDecelerate = CubicBezierEasing(0.05f, 0.7f, 0.1f, 1f)
    val EmphasizedAccelerate = CubicBezierEasing(0.3f, 0f, 0.8f, 0.15f)

    // Duration tokens
    val Short1 = tween<Float>(50, easing = Standard)
    val Short2 = tween<Float>(100, easing = Standard)
    val Medium1 = tween<Float>(200, easing = Emphasized)
    val Medium2 = tween<Float>(300, easing = Emphasized)
    val Long1 = tween<Float>(400, easing = EmphasizedDecelerate)
    val Long2 = tween<Float>(500, easing = EmphasizedDecelerate)
}

val LocalAzHuaMotion = staticCompositionLocalOf { AzHuaMotion }

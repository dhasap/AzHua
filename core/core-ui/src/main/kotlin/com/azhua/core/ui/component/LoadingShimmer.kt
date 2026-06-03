package com.azhua.core.ui.component

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import com.azhua.core.ui.theme.*

private val shimmerColors = listOf(
    ColorSurfaceVariant,
    ColorSurfaceContainerHigh,
    ColorSurfaceVariant,
)

@Composable
private fun shimmerBrush(): Brush {
    val transition = rememberInfiniteTransition(label = "shimmer")
    val translateAnim by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart,
        ),
        label = "shimmer_translate",
    )

    return Brush.linearGradient(
        colors = shimmerColors,
        start = Offset(translateAnim - 200f, translateAnim - 200f),
        end = Offset(translateAnim, translateAnim),
    )
}

@Composable
fun ShimmerBox(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .background(
                brush = shimmerBrush(),
                shape = MaterialTheme.shapes.medium,
            )
    )
}

@Composable
fun ShimmerDonghuaGrid(
    columns: Int = 2,
    itemCount: Int = 6,
    modifier: Modifier = Modifier,
) {
    val rows = (itemCount + columns - 1) / columns
    Column(modifier = modifier.padding(16.dp)) {
        repeat(rows) { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                repeat(columns) { col ->
                    val index = row * columns + col
                    if (index < itemCount) {
                        ShimmerBox(
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(2f / 3f)
                        )
                    } else {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun ShimmerEpisodeList(
    itemCount: Int = 8,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.padding(16.dp)) {
        repeat(itemCount) {
            ShimmerBox(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(72.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

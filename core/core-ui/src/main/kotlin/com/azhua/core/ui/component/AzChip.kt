package com.azhua.core.ui.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.azhua.core.ui.theme.*

enum class ChipVariant { Filter, Input, Suggestion, Genre }

@Composable
fun AzChip(
    text: String,
    modifier: Modifier = Modifier,
    variant: ChipVariant = ChipVariant.Filter,
    selected: Boolean = false,
    leadingIcon: ImageVector? = null,
    onClick: () -> Unit,
) {
    val containerColor by animateColorAsState(
        targetValue = if (selected) ColorPrimaryContainer else ColorSurfaceVariant,
        label = "chip_bg"
    )
    val labelColor by animateColorAsState(
        targetValue = if (selected) ColorPrimary else ColorTextSecondary,
        label = "chip_text"
    )
    val borderColor by animateColorAsState(
        targetValue = if (selected) ColorPrimary else ColorOutlineVariant,
        label = "chip_border"
    )

    FilterChip(
        selected = selected,
        onClick = onClick,
        label = {
            Text(
                text = text,
                style = MaterialTheme.typography.labelMedium,
                color = labelColor,
            )
        },
        leadingIcon = leadingIcon?.let {
            {
                androidx.compose.foundation.layout.Box(
                    modifier = Modifier.padding(end = 2.dp)
                ) {
                    androidx.compose.material3.Icon(
                        imageVector = it,
                        contentDescription = null,
                        tint = labelColor,
                    )
                }
            }
        },
        modifier = modifier,
        shape = MaterialTheme.shapes.small,
        colors = FilterChipDefaults.filterChipColors(
            containerColor = containerColor,
            selectedContainerColor = containerColor,
        ),
        border = BorderStroke(1.dp, borderColor),
    )
}

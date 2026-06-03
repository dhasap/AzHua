package com.azhua.core.ui.component

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.unit.dp
import com.azhua.core.model.Category
import com.azhua.core.ui.theme.*

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CategoryHeader(
    category: Category,
    isExpanded: Boolean,
    itemCount: Int,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier,
    onLongPress: (() -> Unit)? = null,
) {
    val rotation by animateFloatAsState(
        targetValue = if (isExpanded) 90f else 0f,
        label = "chevron_rotation",
    )

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(ColorSurface)
            .combinedClickable(
                onClick = onToggle,
                onLongClick = onLongPress,
            )
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .animateContentSize(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = Icons.Filled.ChevronRight,
            contentDescription = if (isExpanded) "Collapse" else "Expand",
            tint = ColorTextSecondary,
            modifier = Modifier
                .size(20.dp)
                .rotate(rotation),
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = category.name,
            style = MaterialTheme.typography.titleMedium,
            color = ColorTextPrimary,
            modifier = Modifier.weight(1f),
        )

        Text(
            text = "($itemCount)",
            style = MaterialTheme.typography.bodySmall,
            color = ColorTextSecondary,
        )
    }
}

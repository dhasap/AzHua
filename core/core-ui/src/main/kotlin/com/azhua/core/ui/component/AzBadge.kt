package com.azhua.core.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.azhua.core.ui.theme.*

enum class BadgeVariant { Notification, New, Update, HD }

@Composable
fun AzBadge(
    text: String,
    modifier: Modifier = Modifier,
    variant: BadgeVariant = BadgeVariant.New,
) {
    val (bgColor, textColor) = when (variant) {
        BadgeVariant.Notification -> ColorError to Color.White
        BadgeVariant.New -> ColorPrimary to ColorOnPrimary
        BadgeVariant.Update -> ColorSecondary to ColorOnSecondary
        BadgeVariant.HD -> ColorTertiary to Color.White
    }

    Text(
        text = text,
        style = MaterialTheme.typography.labelSmall,
        color = textColor,
        modifier = modifier
            .clip(MaterialTheme.shapes.small)
            .background(bgColor)
            .padding(horizontal = 6.dp, vertical = 2.dp)
    )
}

@Composable
fun AzBadge(
    count: Int,
    modifier: Modifier = Modifier,
    variant: BadgeVariant = BadgeVariant.Notification,
) {
    if (count > 0) {
        AzBadge(
            text = if (count > 99) "99+" else count.toString(),
            modifier = modifier,
            variant = variant,
        )
    }
}

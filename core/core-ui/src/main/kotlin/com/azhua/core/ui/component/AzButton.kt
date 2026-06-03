package com.azhua.core.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.size
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.azhua.core.ui.theme.*

enum class ButtonVariant { Primary, Secondary, Outlined, Ghost, Danger }

@Composable
fun AzButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    variant: ButtonVariant = ButtonVariant.Primary,
    leadingIcon: ImageVector? = null,
    isLoading: Boolean = false,
    enabled: Boolean = true,
) {
    val (containerColor, contentColor, borderStroke) = when (variant) {
        ButtonVariant.Primary -> Triple(ColorPrimary, ColorOnPrimary, null)
        ButtonVariant.Secondary -> Triple(ColorSecondaryContainer, ColorSecondary, null)
        ButtonVariant.Outlined -> Triple(Color.Transparent, ColorPrimary, BorderStroke(1.5.dp, ColorPrimary))
        ButtonVariant.Ghost -> Triple(Color.Transparent, ColorTextSecondary, null)
        ButtonVariant.Danger -> Triple(ColorError, Color.White, null)
    }

    val buttonColors = ButtonDefaults.buttonColors(
        containerColor = containerColor,
        contentColor = contentColor,
        disabledContainerColor = containerColor.copy(alpha = 0.5f),
        disabledContentColor = contentColor.copy(alpha = 0.5f),
    )

    Button(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled && !isLoading,
        colors = buttonColors,
        shape = MaterialTheme.shapes.medium,
        border = borderStroke,
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(18.dp),
                color = contentColor,
                strokeWidth = 2.dp,
            )
        } else {
            leadingIcon?.let {
                Icon(
                    imageVector = it,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp),
                )
            }
            Text(text = text, style = MaterialTheme.typography.labelLarge)
        }
    }
}

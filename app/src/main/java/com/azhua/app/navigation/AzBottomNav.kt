package com.azhua.app.navigation

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Extension
import androidx.compose.material.icons.filled.LocalLibrary
import androidx.compose.material.icons.filled.WatchLater
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material.icons.outlined.Extension
import androidx.compose.material.icons.outlined.LocalLibrary
import androidx.compose.material.icons.outlined.WatchLater
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.azhua.core.ui.theme.*

data class AzNavTab(
    val route: String,
    val label: String,
    val icon: ImageVector,
    val selectedIcon: ImageVector,
)

private val tabs = listOf(
    AzNavTab(AzHuaRoutes.LIBRARY, "Pusaka", Icons.Outlined.LocalLibrary, Icons.Filled.LocalLibrary),
    AzNavTab(AzHuaRoutes.DISCOVER, "Jelajah", Icons.Outlined.Explore, Icons.Filled.Explore),
    AzNavTab(AzHuaRoutes.RECENTS, "Terkini", Icons.Outlined.WatchLater, Icons.Filled.WatchLater),
    AzNavTab(AzHuaRoutes.EXTENSIONS, "Paviliun", Icons.Outlined.Extension, Icons.Filled.Extension),
)

@Composable
fun AzBottomNav(
    currentRoute: String,
    onNavigate: (String) -> Unit,
    modifier: Modifier = Modifier,
    notificationCounts: Map<String, Int> = emptyMap(),
) {
    NavigationBar(
        modifier = modifier,
        containerColor = ColorSurface,
        tonalElevation = 0.dp,
    ) {
        tabs.forEach { tab ->
            val selected = currentRoute == tab.route
            val scale by animateFloatAsState(
                targetValue = if (selected) 1.15f else 1f,
                label = "nav_icon_scale",
            )

            NavigationBarItem(
                selected = selected,
                onClick = { onNavigate(tab.route) },
                icon = {
                    BadgedBox(
                        badge = {
                            val count = notificationCounts[tab.route]
                            if (count != null && count > 0) {
                                Badge(
                                    containerColor = ColorError,
                                    contentColor = Color.White,
                                ) {
                                    Text(
                                        text = if (count > 99) "99+" else count.toString(),
                                        style = MaterialTheme.typography.labelSmall,
                                    )
                                }
                            }
                        }
                    ) {
                        Icon(
                            imageVector = if (selected) tab.selectedIcon else tab.icon,
                            contentDescription = tab.label,
                            modifier = Modifier.scale(scale),
                        )
                    }
                },
                label = {
                    Text(
                        text = tab.label,
                        style = MaterialTheme.typography.labelSmall,
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = ColorPrimary,
                    selectedTextColor = ColorPrimary,
                    unselectedIconColor = ColorTextTertiary,
                    unselectedTextColor = ColorTextTertiary,
                    indicatorColor = ColorPrimaryContainer,
                ),
            )
        }
    }
}

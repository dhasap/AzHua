package com.azhua.app.navigation

import android.content.Intent
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.azhua.feature.library.LibraryScreen
import com.azhua.feature.discover.DiscoverScreen
import com.azhua.feature.recents.RecentsScreen
import com.azhua.feature.extensions.ExtensionScreen
import com.azhua.feature.detail.DetailScreen
import com.azhua.feature.discover.BrowseSourceScreen
import com.azhua.feature.settings.SettingsScreen
import com.azhua.feature.player.PlayerActivity

@Composable
fun AzHuaNavHost(
    navController: NavHostController = rememberNavController(),
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val context = LocalContext.current

    val showBottomNav = currentRoute in listOf(
        AzHuaRoutes.LIBRARY,
        AzHuaRoutes.DISCOVER,
        AzHuaRoutes.RECENTS,
        AzHuaRoutes.EXTENSIONS,
    )

    // Helper to launch player
    val launchPlayer: (Long, Long) -> Unit = { donghuaId, episodeId ->
        val intent = PlayerActivity.createIntent(context, donghuaId, episodeId)
        context.startActivity(intent)
    }

    Scaffold(
        bottomBar = {
            if (showBottomNav) {
                AzBottomNav(
                    currentRoute = currentRoute ?: AzHuaRoutes.LIBRARY,
                    onNavigate = { route ->
                        navController.navigate(route) {
                            popUpTo(AzHuaRoutes.LIBRARY) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = AzHuaRoutes.LIBRARY,
            modifier = Modifier.padding(paddingValues),
            enterTransition = {
                fadeIn(tween(300)) + slideInVertically(tween(300)) { it / 10 }
            },
            exitTransition = {
                fadeOut(tween(200))
            },
            popEnterTransition = {
                fadeIn(tween(300))
            },
            popExitTransition = {
                fadeOut(tween(200)) + slideOutVertically(tween(200)) { it / 10 }
            },
        ) {
            // ========================
            // Bottom Nav Destinations
            // ========================
            composable(AzHuaRoutes.LIBRARY) {
                LibraryScreen(
                    onNavigateToDetail = { navController.navigate(AzHuaRoutes.detail(it)) }
                )
            }

            composable(AzHuaRoutes.DISCOVER) {
                DiscoverScreen(
                    onNavigateToDetail = { navController.navigate(AzHuaRoutes.detail(it)) },
                    onNavigateToSource = { navController.navigate(AzHuaRoutes.browseSource(it)) }
                )
            }

            composable(AzHuaRoutes.RECENTS) {
                RecentsScreen(
                    onNavigateToDetail = { navController.navigate(AzHuaRoutes.detail(it)) },
                    onNavigateToPlayer = { donghuaId, episodeId ->
                        launchPlayer(donghuaId, episodeId)
                    }
                )
            }

            composable(AzHuaRoutes.EXTENSIONS) {
                ExtensionScreen(
                    onNavigateToSettings = { navController.navigate(AzHuaRoutes.extensionSettings(it)) }
                )
            }

            // ========================
            // Detail Screen
            // ========================
            composable(
                route = AzHuaRoutes.DETAIL,
                arguments = listOf(navArgument("donghuaId") { type = NavType.LongType }),
            ) { backStackEntry ->
                val donghuaId = backStackEntry.arguments?.getLong("donghuaId") ?: return@composable
                DetailScreen(
                    donghuaId = donghuaId,
                    onBack = { navController.popBackStack() },
                    onPlayEpisode = { episodeId ->
                        launchPlayer(donghuaId, episodeId)
                    }
                )
            }

            // ========================
            // Browse Source Screen
            // ========================
            composable(
                route = AzHuaRoutes.BROWSE_SOURCE,
                arguments = listOf(navArgument("sourceId") { type = NavType.StringType }),
            ) { backStackEntry ->
                val sourceId = backStackEntry.arguments?.getString("sourceId") ?: return@composable
                BrowseSourceScreen(
                    sourceId = sourceId,
                    onBack = { navController.popBackStack() },
                    onNavigateToDetail = { navController.navigate(AzHuaRoutes.detail(it)) },
                )
            }

            // ========================
            // Settings Screen
            // ========================
            composable(AzHuaRoutes.SETTINGS) {
                SettingsScreen(
                    onBack = { navController.popBackStack() },
                    onNavigateTo = { navController.navigate(it) },
                )
            }

            composable(AzHuaRoutes.STATS) {
                // TODO: StatsScreen
            }

            composable(AzHuaRoutes.BACKUP) {
                // TODO: BackupRestoreScreen
            }
        }
    }
}

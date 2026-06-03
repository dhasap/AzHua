package com.azhua.app.navigation

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.azhua.core.ui.theme.AzHuaMotion
import com.azhua.feature.library.LibraryScreen
import com.azhua.feature.discover.DiscoverScreen
import com.azhua.feature.recents.RecentsScreen
import com.azhua.feature.extensions.ExtensionScreen
import com.azhua.feature.detail.DetailScreen

@Composable
fun AzHuaNavHost(
    navController: NavHostController = rememberNavController(),
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val showBottomNav = currentRoute in listOf(
        AzHuaRoutes.LIBRARY,
        AzHuaRoutes.DISCOVER,
        AzHuaRoutes.RECENTS,
        AzHuaRoutes.EXTENSIONS,
    )

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
            // Bottom Nav Destinations
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
                        // TODO: Launch PlayerActivity
                    }
                )
            }

            composable(AzHuaRoutes.EXTENSIONS) {
                ExtensionScreen(
                    onNavigateToSettings = { navController.navigate(AzHuaRoutes.extensionSettings(it)) }
                )
            }

            // Detail Screen
            composable(
                route = AzHuaRoutes.DETAIL,
                arguments = listOf(navArgument("donghuaId") { type = NavType.LongType }),
            ) { backStackEntry ->
                val donghuaId = backStackEntry.arguments?.getLong("donghuaId") ?: return@composable
                DetailScreen(
                    donghuaId = donghuaId,
                    onBack = { navController.popBackStack() },
                    onPlayEpisode = { episodeId ->
                        // TODO: Launch PlayerActivity
                    }
                )
            }

            // Browse Source Screen
            composable(
                route = AzHuaRoutes.BROWSE_SOURCE,
                arguments = listOf(navArgument("sourceId") { type = NavType.StringType }),
            ) { backStackEntry ->
                val sourceId = backStackEntry.arguments?.getString("sourceId") ?: return@composable
                // TODO: BrowseSourceScreen
            }

            // Settings
            composable(AzHuaRoutes.SETTINGS) {
                // TODO: SettingsScreen
            }

            composable(AzHuaRoutes.STATS) {
                // TODO: StatsScreen
            }
        }
    }
}

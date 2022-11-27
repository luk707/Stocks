package dev.lukeharris.stocks

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

sealed class Screen(
    val route: String,
    val icon: ImageVector,
    @StringRes val resourceId: Int
) {
    object Stocks : Screen("stocks", Icons.Filled.Home, R.string.stocks)
    object Settings : Screen("settings", Icons.Filled.Settings, R.string.settings)
}

val bottomNavigationItems = listOf(
    Screen.Stocks,
    Screen.Settings
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StocksApp() {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val navController = rememberNavController()

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LargeTopAppBar(
                title = { Text("Stocks") },
                scrollBehavior = scrollBehavior,
            )
        },
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                bottomNavigationItems.forEach { navigationItem ->
                    val navigationItemLabel = stringResource(id = navigationItem.resourceId)

                    NavigationBarItem(
                        icon = { Icon(navigationItem.icon, contentDescription = navigationItemLabel) },
                        label = { Text(navigationItemLabel) },
                        selected = currentDestination?.hierarchy?.any { it.route == navigationItem.route } == true,
                        onClick = {
                            navController.navigate(navigationItem.route) {
                                // Pop up to the start destination of the graph to
                                // avoid building up a large stack of destinations
                                // on the back stack as users select items
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                // Avoid multiple copies of the same destination when
                                // reselecting the same item
                                launchSingleTop = true
                                // Restore state when reselecting a previously selected item
                                restoreState = true
                            }
                        }
                    )
                }
            }
        },
        content = { innerPadding ->
            NavHost(navController, startDestination = Screen.Stocks.route, Modifier.padding(innerPadding)) {
                composable(Screen.Stocks.route) { Text("Stocks") }
                composable(Screen.Settings.route) { Text("Settings") }
            }
        }
    )
}

@Preview(showSystemUi = true)
@Composable
fun DefaultPreview() {
    StocksApp()
}

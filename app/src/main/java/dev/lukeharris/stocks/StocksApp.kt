package dev.lukeharris.stocks

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
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
    @DrawableRes val icon: Int,
    @StringRes val label: Int
) {
    object Stocks : Screen(
        "stocks",
        R.drawable.ic_baseline_show_chart_24,
        R.string.stocks
    )
    object Forex : Screen(
        "forex",
        R.drawable.ic_baseline_currency_exchange_24,
        R.string.forex
    )
    object Settings : Screen(
        "settings",
        R.drawable.ic_baseline_settings_24,
        R.string.settings
    )
}

val bottomNavigationScreens = listOf(
    Screen.Stocks,
    Screen.Forex
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StocksApp() {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val navController = rememberNavController()

    fun navigateTo(route: String): () -> Unit {
        return {
            navController.navigate(route) {
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
    }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LargeTopAppBar(
                title = { Text("Stocks") },
                scrollBehavior = scrollBehavior,
                actions = {
                    IconButton(onClick = navigateTo(Screen.Settings.route)) {
                        Icon(
                            painter = painterResource(id = Screen.Settings.icon),
                            contentDescription = stringResource(
                                id = Screen.Settings.label
                            )
                        )
                    }
                },
            )
        },
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                bottomNavigationScreens.forEach { screen ->
                    val navigationItemLabel = stringResource(id = screen.label)

                    NavigationBarItem(
                        icon = {
                            Icon(
                                painter = painterResource(id = screen.icon),
                                contentDescription = navigationItemLabel
                            )
                        },
                        label = { Text(navigationItemLabel) },
                        selected = currentDestination
                            ?.hierarchy
                            ?.any {
                                it.route == screen.route
                            } == true,
                        onClick = navigateTo(screen.route)
                    )
                }
            }
        },
        content = { innerPadding ->
            NavHost(
                navController,
                startDestination = Screen.Stocks.route,
                Modifier.padding(innerPadding)
            ) {
                composable(Screen.Stocks.route) { StocksScreen() }
                composable(Screen.Forex.route) { Text("Forex") }
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

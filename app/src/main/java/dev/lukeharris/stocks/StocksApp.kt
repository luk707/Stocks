package dev.lukeharris.stocks

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.*
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

sealed class Screen(
    val route: String,
    @StringRes val label: Int,
    @DrawableRes val icon: Int? = null
) {
    object Stocks : Screen(
        "stocks",
        R.string.stocks,
        R.drawable.ic_baseline_show_chart_24
    )
    object StocksSearch : Screen(
        "stocksSearch",
        R.string.stocksSearch
    )
    object Forex : Screen(
        "forex",
        R.string.forex,
        R.drawable.ic_baseline_currency_exchange_24
    )
    object Settings : Screen(
        "settings",
        R.string.settings,
        R.drawable.ic_baseline_settings_24
    )
    object StyleSettings : Screen(
        "settings/style",
        R.string.styleSettings
    )
    object Info : Screen(
        "settings/info",
        R.string.info
    )
}

val bottomNavigationScreens = listOf(
    Screen.Stocks,
    Screen.Forex
)

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun StocksApp() {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val navController = rememberNavController()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

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
                title = {
                    Text(when (currentDestination?.route) {
                        Screen.Stocks.route ->
                            stringResource(id = Screen.Stocks.label)
                        Screen.StocksSearch.route ->
                            stringResource(id = Screen.StocksSearch.label)
                        Screen.Forex.route ->
                            stringResource(id = Screen.Forex.label)
                        Screen.Settings.route ->
                            stringResource(id = Screen.Settings.label)
                        Screen.StyleSettings.route ->
                            stringResource(id = Screen.StyleSettings.label)
                        Screen.Info.route ->
                            stringResource(id = Screen.Info.label)
                        else -> "Stocks"
                    })
                },
                scrollBehavior = scrollBehavior,
                navigationIcon = {
                    when (currentDestination?.route) {
                        Screen.StocksSearch.route,
                        Screen.Info.route,
                        Screen.StyleSettings.route ->
                            IconButton(onClick = { navController.popBackStack() }) {
                                Icon(
                                    Icons.Filled.ArrowBack,
                                    contentDescription = "Back"
                                )
                            }
                    }
                },
                actions = {
                    when (currentDestination?.route) {
                        Screen.Settings.route,
                        Screen.Info.route,
                        Screen.StyleSettings.route ->
                            null
                        else ->
                            IconButton(onClick = navigateTo(Screen.Settings.route)) {
                                Icon(
                                    painter = painterResource(id = Screen.Settings.icon ?: R.drawable.ic_baseline_show_chart_24),
                                    contentDescription = stringResource(
                                        id = Screen.Settings.label
                                    )
                                )
                            }
                    }
                },
            )
        },
        bottomBar = {
            AnimatedVisibility(
                visible = when (currentDestination?.route) {
                    Screen.StocksSearch.route ->
                        false
                    else ->
                        true
                },
                enter = slideInVertically(initialOffsetY = { 200 }),
                exit = slideOutVertically(targetOffsetY = { 200 })
            ) {
                NavigationBar {
                    bottomNavigationScreens.forEach { screen ->
                        val navigationItemLabel = stringResource(id = screen.label)

                        NavigationBarItem(
                            icon = {
                                Icon(
                                    painter = painterResource(
                                        id = screen.icon ?: R.drawable.ic_baseline_show_chart_24
                                    ),
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
            }
        },
        content = { innerPadding ->
            NavHost(
                navController,
                startDestination = Screen.Stocks.route,
                Modifier.padding(innerPadding)
            ) {
                composable(Screen.Stocks.route) { StocksScreen(navController = navController) }
                composable(Screen.StocksSearch.route) { StocksSearch() }
                composable(Screen.Forex.route) { Text("Forex") }
                composable(Screen.Settings.route) { SettingsScreen(navController = navController) }
                composable(Screen.StyleSettings.route) { Text("Style") }
                composable(Screen.Info.route) { Text("Info") }
            }
        }
    )
}

@Preview(showSystemUi = true)
@Composable
fun DefaultPreview() {
    StocksApp()
}

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import dev.lukeharris.stocks.ui.theme.Cousine

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
    object StocksDetail : Screen(
        "stocks/{ticker}",
        R.string.stocksDetail
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
                    with (currentDestination?.route) {
                        when {
                            equals(Screen.Stocks.route) ->
                                Text(stringResource(id = Screen.Stocks.label))
                            this?.startsWith("stocks/") ?: false ->
                                navBackStackEntry?.arguments?.getString("ticker")?.let { ticker ->
                                    Text(
                                        ticker,
                                        fontFamily = Cousine,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            equals(Screen.StocksSearch.route) ->
                                Text(stringResource(id = Screen.StocksSearch.label))
                            equals(Screen.Forex.route) ->
                                Text(stringResource(id = Screen.Forex.label))
                            equals(Screen.Settings.route) ->
                                Text(stringResource(id = Screen.Settings.label))
                            equals(Screen.StyleSettings.route) ->
                                Text(stringResource(id = Screen.StyleSettings.label))
                            equals(Screen.Info.route) ->
                                Text(stringResource(id = Screen.Info.label))
                            else -> Text("Stocks")
                        }
                    }
                },
                scrollBehavior = scrollBehavior,
                navigationIcon = {
                    with (currentDestination?.route) {
                        when {
                            equals(Screen.StocksSearch.route) ||
                            equals(Screen.Info.route) ||
                            equals(Screen.StyleSettings.route) ||
                            this?.startsWith("stocks/") ?: false ->
                                IconButton(onClick = { navController.popBackStack() }) {
                                    Icon(
                                        Icons.Filled.ArrowBack,
                                        contentDescription = "Back"
                                    )
                                }
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
                                    it.route?.startsWith(screen.route) ?: false
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
                composable(Screen.StocksDetail.route, arguments = listOf(
                    navArgument("ticker") {
                        type = NavType.StringType
                    }
                )) { backStackEntry ->
                    StocksDetail(ticker = backStackEntry.arguments?.getString("ticker") ?: "")
                }
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

package dev.lukeharris.stocks.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun StocksTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {

    val systemUiController = rememberSystemUiController()

    val colors = if (darkTheme) {
        dynamicDarkColorScheme(LocalContext.current)
    } else {
        dynamicLightColorScheme(LocalContext.current)
    }

    systemUiController.setSystemBarsColor(Color.Transparent, darkIcons = !darkTheme)
    systemUiController.setNavigationBarColor(colors.surfaceColorAtElevation(NavigationBarDefaults.Elevation))

    MaterialTheme(
        colorScheme = colors,
        content = content
    )
}
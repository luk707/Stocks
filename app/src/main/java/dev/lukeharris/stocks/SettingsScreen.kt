package dev.lukeharris.stocks

import androidx.annotation.DrawableRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController

data class SettingsScreenMenuItem(
    val route: String,
    val headline: String,
    val supportingText: String,
    @DrawableRes val icon: Int,
)

val settingsScreenMenuItems = listOf(
    SettingsScreenMenuItem(
        Screen.StyleSettings.route,
        "Style",
        "Colours, themes and other customisation options.",
        R.drawable.ic_baseline_palette_24
    ),
    SettingsScreenMenuItem(
        Screen.Info.route,
        "Info",
        "Version, third party licenses and more.",
        R.drawable.ic_baseline_info_24
    ),
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    navController: NavHostController
) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
    ) {
        items(settingsScreenMenuItems) { item ->
            ListItem(
                modifier = Modifier.clickable {
                    navController.navigate(item.route)
                },
                leadingContent = {
                    Icon(
                        painter = painterResource(id = item.icon),
                        contentDescription = item.headline
                    )
                },
                headlineText = { Text(item.headline) },
                supportingText = { Text(item.supportingText) }
            )
        }
    }
}

package dev.lukeharris.stocks

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsInfoScreen() {
    Column {
        ListItem(
            headlineText = { Text("App version") },
            supportingText = { Text(BuildConfig.VERSION_NAME) }
        )
    }
}
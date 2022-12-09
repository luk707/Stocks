package dev.lukeharris.stocks

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.Crossfade
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import dev.lukeharris.stocks.ui.theme.Cousine

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun StocksDetail(ticker: String) {
    var selectedRange by remember { mutableStateOf("1D") }

    Column {
        Row(
            modifier = Modifier
                .padding(18.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            listOf("1D", "1W", "1M", "3M", "6M", "1Y").forEach { range ->
                AnimatedContent(targetState =
                    if (range == selectedRange)
                        MaterialTheme.colorScheme.surfaceVariant
                    else
                        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0f)
                ) { bg ->
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(percent = 100))
                            .background(bg)
                            .clickable { selectedRange = range }
                            .padding(PaddingValues(horizontal = 12.dp, vertical = 6.dp))
                    ) {
                        Text(range, fontFamily = Cousine)
                    }
                }
            }
        }
        Box(
            modifier = Modifier
                .padding(horizontal = 18.dp)
                .clip(RoundedCornerShape(size = 12.dp))
                .background(
                    MaterialTheme.colorScheme.surfaceColorAtElevation(
                        NavigationBarDefaults.Elevation
                    )
                )

        ) {
            Crossfade(targetState = selectedRange) { range ->
                CandlestickChart(
                    modifier = Modifier
                        .padding(10.dp)
                        .fillMaxWidth()
                        .height(250.dp),
                    bodyColorPriceIncrease = MaterialTheme.colorScheme.tertiary,
                    bodyColorPriceDecrease = MaterialTheme.colorScheme.error,
                    wickColor = MaterialTheme.colorScheme.onSurface,
                    data = when (range) {
                        else -> CANDLESTICK_TEST_DATA
                    }
                )
            }
        }
    }
}
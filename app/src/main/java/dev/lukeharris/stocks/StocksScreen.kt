package dev.lukeharris.stocks

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

data class Ticker (
    val ticker: String,
    val name: String,
    val price: Double,
)

val STOCKS_SCREEN_TEST_DATA = listOf(
    Ticker(
        ticker = "AAPL",
        name = "Apple Inc.",
        price = 151.15
    ),
    Ticker(
        ticker = "META",
        name = "Meta Platforms, Inc. Class A Common Stock",
        price = 112.34
    ),
    Ticker(
        ticker = "TSLA",
        name = "Tesla, Inc. Common Stock",
        price = 184.8
    ),
    Ticker(
        ticker = "ASML",
        name = "ASML Holding NV",
        price = 604.12
    ),
    Ticker(
        ticker = "GME",
        name = "GameStop Corp. Class A",
        price = 26.75
    )
)

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun StocksScreen() {
    val numberFormat = NumberFormat.getCurrencyInstance(Locale.US)
    numberFormat.maximumFractionDigits = 2

    val list: MutableList<Ticker> = ArrayList()
    list.addAll(STOCKS_SCREEN_TEST_DATA)
    list.addAll(STOCKS_SCREEN_TEST_DATA)
    list.addAll(STOCKS_SCREEN_TEST_DATA)
    list.addAll(STOCKS_SCREEN_TEST_DATA)

    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
    ) {
        items(list) { ticker ->
            ListItem(
                headlineText = { Text(ticker.ticker) },
                overlineText = { Text(ticker.name) },
                supportingText = { Text(numberFormat.format(ticker.price)) },
                trailingContent = {
                    Sparkline(
                        color = if (ticker.ticker == "META")
                            MaterialTheme.colorScheme.error else
                                MaterialTheme.colorScheme.tertiary
                    )
                }
            )
        }
    }
}

package dev.lukeharris.stocks

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import dev.lukeharris.stocks.ui.theme.Cousine
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
    ),
    Ticker(
        ticker = "MSFT",
        name = "Microsoft Corp",
        price = 26.75
    ),
    Ticker(
        ticker = "AMZN",
        name = "Amazon.Com Inc",
        price = 26.75
    ),
    Ticker(
        ticker = "SONY",
        name = "Sony Group Corporation American Depositary Shares (Each Representing One Share of Dollar Validated Common Stock)",
        price = 26.75
    )
)

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun StocksScreen(
    navController: NavHostController
) {
    val numberFormat = NumberFormat.getCurrencyInstance(Locale.US)
    numberFormat.maximumFractionDigits = 2

    val list: MutableList<Ticker> = ArrayList()
    list.addAll(STOCKS_SCREEN_TEST_DATA)

    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
    ) {
        stickyHeader {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(PaddingValues(horizontal = 8.dp, vertical = 10.dp))
                    .clip(RoundedCornerShape(percent = 100))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .clickable { navController.navigate(Screen.StocksSearch.route) }
                    .padding(PaddingValues(horizontal = 12.dp, vertical = 12.dp))

            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        modifier = Modifier.padding(start = 3.5.dp),
                        imageVector = Icons.Filled.Search,
                        contentDescription = "Search",
                    )
                    Spacer(Modifier.width(4.dp))
                    Text("Search Stocks", modifier = Modifier.padding(top = 3.25.dp),  color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
        items(list) { ticker ->
            ListItem(
                overlineText = { Text(ticker.name, maxLines = 1, overflow = TextOverflow.Ellipsis) },
                headlineText = { Text(ticker.ticker, fontFamily = Cousine, fontWeight = FontWeight.Bold) },
                supportingText = { Text(numberFormat.format(ticker.price), fontFamily = Cousine) },
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

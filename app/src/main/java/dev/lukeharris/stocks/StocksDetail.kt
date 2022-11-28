package dev.lukeharris.stocks

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

@Composable
fun StocksDetail(ticker: String) {
    Box(
        modifier = Modifier
            .padding(18.dp)
            .clip(RoundedCornerShape(size = 12.dp))
            .background(MaterialTheme.colorScheme.surfaceColorAtElevation(
                NavigationBarDefaults.Elevation))

    ) {
        CandlestickChart(
            modifier = Modifier.padding(10.dp).fillMaxWidth().height(250.dp),
            bodyColorPriceIncrease = MaterialTheme.colorScheme.tertiary,
            bodyColorPriceDecrease = MaterialTheme.colorScheme.error,
            wickColor = MaterialTheme.colorScheme.onSurface
        )
    }
}
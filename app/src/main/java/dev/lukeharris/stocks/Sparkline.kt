package dev.lukeharris.stocks

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlin.math.max
import kotlin.math.min

val SPARKLINE_TEST_DATA = listOf(
    150.18,
    149.8,
    149.8,
    149.87,
    149.32,
    151.09,
    151.5811,
    150.1,
    149.645,
    150.304,
    150.98,
    151.14,
    151.12,
    151.1601,
    151.17,
    151.15
)

@Preview(showBackground = true)
@Composable
fun Sparkline(
    modifier: Modifier = Modifier.width(88.dp).height(44.dp),
    data: List<Double> = SPARKLINE_TEST_DATA,
    color: Color = MaterialTheme.colorScheme.tertiary,
    strokeWidth: Float = 5f
) {
    Spacer(modifier = modifier.drawWithCache {
        val max = data
            .reduce { acc: Double, price: Double -> max(acc, price) }
        val min = data
            .reduce { acc: Double, price: Double -> min(acc, price) }

        fun yAxis(a: Double): Float {
            return remap(
                min,
                max,
                size.height.toDouble() - strokeWidth / 2,
                (strokeWidth / 2).toDouble(),
                a
            ).toFloat()
        }

        onDrawBehind {

            val step = this.size.width / (data.size - 1)
            val path = Path()
            val fillPath = Path()

            fillPath.moveTo(0f, size.height)
            path.moveTo(0f, yAxis(data[0]))

            data.forEachIndexed { index, price ->
                path.lineTo(index * step, yAxis(price))
                fillPath.lineTo(index * step, yAxis(price))
            }

            fillPath.lineTo(size.width, size.height)

            drawPath(path, color, style = Stroke(
                width = strokeWidth,
                cap = StrokeCap.Round,
                join = StrokeJoin.Round
            ))
            drawPath(
                fillPath,
                Brush.verticalGradient(
                    listOf(
                        Color(color.red, color.green, color.blue, 0.175f),
                        Color.Transparent
                    )
                )
            )
        }
    })
}

package dev.lukeharris.stocks

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import java.lang.Double.max
import java.lang.Double.min

data class Candle(
    val c: Double,
    val h: Double,
    val l: Double,
    val n: Int,
    val o: Double,
    val t: Long,
    val v: Long,
    val vw: Double,
)

val CANDLESTICK_TEST_DATA = listOf(
    Candle(c = 148.34, h = 149.26, l = 148.21, n = 673, o = 148.98, t = 1668675600000, v = 33151, vw = 148.7864),
    Candle(c = 147.75, h = 148.68, l = 147.65, n = 620, o = 148.49, t = 1668679200000, v = 30183, vw = 148.1555),
    Candle(c = 147.8, h = 148.15, l = 147.41, n = 956, o = 147.73, t = 1668682800000, v = 42904, vw = 147.7561),
    Candle(c = 147.0, h = 147.87, l = 146.72, n = 2830, o = 147.87, t = 1668686400000, v = 150070, vw = 147.1887),
    Candle(c = 145.88, h = 149.97, l = 145.81, n = 9410, o = 147.85, t = 1668690000000, v = 580426, vw = 146.3856),
    Candle(c = 147.49, h = 148.0399, l = 145.66, n = 79113, o = 145.87, t = 1668693600000, v = 7895620, vw = 147.3838),
    Candle(c = 148.15, h = 148.81, l = 147.3204, n = 74770, o = 147.4891, t = 1668697200000, v = 8508664, vw = 148.2491),
    Candle(c = 149.995, h = 150.0554, l = 148.09, n = 75395, o = 148.14, t = 1668700800000, v = 9560378, vw = 149.0338),
    Candle(c = 151.16, h = 151.3499, l = 149.61, n = 67263, o = 149.9901, t = 1668704400000, v = 8917230, vw = 150.2202),
    Candle(c = 151.085, h = 151.48, l = 150.7908, n = 64147, o = 151.1582, t = 1668708000000, v = 7441454, vw = 151.1776),
    Candle(c = 150.2208, h = 151.17, l = 149.555, n = 78870, o = 151.085, t = 1668711600000, v = 11023000, vw = 150.1721),
    Candle(c = 150.75, h = 150.85, l = 149.53, n = 103041, o = 150.2104, t = 1668715200000, v = 12872641, vw = 150.1368),
    Candle(c = 151.08, h = 151.14, l = 150.25, n = 2750, o = 150.72, t = 1668718800000, v = 7533156, vw = 150.7183),
    Candle(c = 151.03, h = 151.17, l = 150.72, n = 750, o = 151.0801, t = 1668722400000, v = 111471, vw = 150.9235),
    Candle(c = 150.74, h = 151.17, l = 150.73, n = 862, o = 151.05, t = 1668726000000, v = 68926, vw = 150.9669),
    Candle(c = 150.9, h = 151.07, l = 150.7, n = 716, o = 150.72, t = 1668729600000, v = 51664, vw = 150.8593),
    Candle(c = 150.8, h = 151.22, l = 150.17, n = 777, o = 150.17, t = 1668762000000, v = 50764, vw = 150.8552),
    Candle(c = 151.6, h = 151.68, l = 150.73, n = 708, o = 150.81, t = 1668765600000, v = 43628, vw = 151.166),
    Candle(c = 151.3, h = 151.58, l = 151.28, n = 369, o = 151.5, t = 1668769200000, v = 38538, vw = 151.3565),
    Candle(c = 151.55, h = 152.07, l = 151.29, n = 2219, o = 151.31, t = 1668772800000, v = 142058, vw = 151.7227),
    Candle(c = 151.9, h = 152.0885, l = 151.3, n = 4614, o = 151.57, t = 1668776400000, v = 243827, vw = 151.8118),
    Candle(c = 151.48, h = 152.7, l = 150.775, n = 77824, o = 151.92, t = 1668780000000, v = 9651594, vw = 151.6405),
    Candle(c = 150.9905, h = 151.5199, l = 149.97, n = 88531, o = 151.48, t = 1668783600000, v = 11091951, vw = 150.6949),
    Candle(c = 151.42, h = 151.98, l = 150.57, n = 74017, o = 150.99, t = 1668787200000, v = 9789622, vw = 151.3908),
    Candle(c = 150.65, h = 151.59, l = 150.35, n = 51995, o = 151.41, t = 1668790800000, v = 6891097, vw = 150.8618),
    Candle(c = 150.99, h = 151.2799, l = 150.54, n = 48609, o = 150.6509, t = 1668794400000, v = 5551737, vw = 150.8928),
    Candle(c = 151.21, h = 151.3079, l = 150.86, n = 48723, o = 150.995, t = 1668798000000, v = 5849803, vw = 151.0597),
    Candle(c = 151.31, h = 151.91, l = 150.78, n = 110647, o = 151.22, t = 1668801600000, v = 14223952, vw = 151.3929),
    Candle(c = 151.11, h = 151.37, l = 150.97, n = 2278, o = 151.31, t = 1668805200000, v = 3799633, vw = 151.2808),
    Candle(c = 151.0034, h = 151.29, l = 151.0, n = 379, o = 151.09, t = 1668808800000, v = 291816, vw = 151.2693),
    Candle(c = 150.95, h = 151.09, l = 150.9, n = 327, o = 151.03, t = 1668812400000, v = 16014, vw = 150.964),
    Candle(c = 150.88, h = 150.98, l = 150.85, n = 283, o = 150.91, t = 1668816000000, v = 18221, vw = 150.9236),
    Candle(c = 150.17, h = 150.65, l = 150.01, n = 756, o = 150.1, t = 1669021200000, v = 29702, vw = 150.1777),
    Candle(c = 149.69, h = 150.2, l = 149.08, n = 509, o = 150.16, t = 1669024800000, v = 21963, vw = 149.748),
    Candle(c = 149.35, h = 149.86, l = 149.28, n = 337, o = 149.7, t = 1669028400000, v = 13215, vw = 149.5472),
    Candle(c = 149.77, h = 149.88, l = 149.36, n = 1462, o = 149.42, t = 1669032000000, v = 81495, vw = 149.637),
    Candle(c = 150.27, h = 150.79, l = 149.4315, n = 4947, o = 150.65, t = 1669035600000, v = 312366, vw = 150.1427),
    Candle(c = 149.64, h = 150.49, l = 148.46, n = 116157, o = 150.2, t = 1669039200000, v = 8841056, vw = 149.3923),
    Candle(c = 148.0601, h = 149.96, l = 147.78, n = 94611, o = 149.66, t = 1669042800000, v = 9846756, vw = 148.5684),
    Candle(c = 148.076, h = 148.74, l = 147.75, n = 63863, o = 148.06, t = 1669046400000, v = 6948546, vw = 148.2893),
    Candle(c = 147.99, h = 148.36, l = 147.715, n = 47772, o = 148.075, t = 1669050000000, v = 5178843, vw = 148.05),
    Candle(c = 148.51, h = 149.03, l = 147.905, n = 49954, o = 147.98, t = 1669053600000, v = 6066716, vw = 148.5298),
    Candle(c = 148.39, h = 148.76, l = 148.23, n = 38604, o = 148.5, t = 1669057200000, v = 4390034, vw = 148.4763),
    Candle(c = 148.02, h = 148.6, l = 147.88, n = 80632, o = 148.4, t = 1669060800000, v = 9218135, vw = 148.1899),
    Candle(c = 148.05, h = 148.29, l = 147.8, n = 1888, o = 148.01, t = 1669064400000, v = 2441353, vw = 148.0142),
    Candle(c = 148.02, h = 148.09, l = 148.01, n = 472, o = 148.05, t = 1669068000000, v = 62887, vw = 148.0202),
    Candle(c = 148.03, h = 148.1, l = 148.01, n = 388, o = 148.05, t = 1669071600000, v = 26136, vw = 148.0393),
    Candle(c = 148.12, h = 148.27, l = 148.06, n = 605, o = 148.06, t = 1669075200000, v = 51240, vw = 148.1737),
    Candle(c = 148.4, h = 148.69, l = 147.65, n = 585, o = 147.91, t = 1669107600000, v = 26179, vw = 148.0843),
    Candle(c = 148.23, h = 148.44, l = 148.03, n = 185, o = 148.44, t = 1669111200000, v = 8372, vw = 148.1773),
    Candle(c = 147.95, h = 148.23, l = 147.7, n = 478, o = 148.23, t = 1669114800000, v = 28301, vw = 147.9084),
    Candle(c = 148.22, h = 148.35, l = 147.9, n = 1326, o = 147.99, t = 1669118400000, v = 85060, vw = 148.129),
    Candle(c = 148.43, h = 148.64, l = 146.86, n = 3430, o = 147.95, t = 1669122000000, v = 196532, vw = 148.3967),
    Candle(c = 147.51, h = 148.55, l = 146.925, n = 72991, o = 148.45, t = 1669125600000, v = 6353086, vw = 147.6237),
    Candle(c = 148.71, h = 148.83, l = 147.41, n = 72488, o = 147.5, t = 1669129200000, v = 8229066, vw = 148.2318),
    Candle(c = 148.865, h = 148.98, l = 148.38, n = 48422, o = 148.715, t = 1669132800000, v = 5159658, vw = 148.7233),
    Candle(c = 149.2, h = 149.53, l = 148.8471, n = 44683, o = 148.86, t = 1669136400000, v = 5035465, vw = 149.2364),
    Candle(c = 149.97, h = 150.04, l = 149.04, n = 41813, o = 149.19, t = 1669140000000, v = 4826358, vw = 149.6811),
    Candle(c = 149.79, h = 150.05, l = 149.46, n = 40219, o = 149.97, t = 1669143600000, v = 5107570, vw = 149.6899),
    Candle(c = 150.25, h = 150.42, l = 149.74, n = 73743, o = 149.7905, t = 1669147200000, v = 8726278, vw = 150.0839),
    Candle(c = 150.03, h = 150.24, l = 150.01, n = 1584, o = 150.18, t = 1669150800000, v = 1411788, vw = 150.1775),
    Candle(c = 150.05, h = 150.08, l = 149.99, n = 452, o = 150.02, t = 1669154400000, v = 24557, vw = 150.0359),
    Candle(c = 150.09, h = 150.14, l = 150.01, n = 699, o = 150.06, t = 1669158000000, v = 37424, vw = 150.0687),
    Candle(c = 150.1, h = 150.35, l = 150.05, n = 636, o = 150.06, t = 1669161600000, v = 48172, vw = 150.1132),
    Candle(c = 150.18, h = 150.26, l = 149.68, n = 665, o = 149.75, t = 1669194000000, v = 42865, vw = 150.0113),
    Candle(c = 149.8, h = 150.09, l = 149.75, n = 372, o = 150.08, t = 1669197600000, v = 20226, vw = 149.9294),
    Candle(c = 149.8, h = 149.91, l = 149.54, n = 562, o = 149.89, t = 1669201200000, v = 34266, vw = 149.7611),
    Candle(c = 149.87, h = 150.0, l = 149.62, n = 2003, o = 149.82, t = 1669204800000, v = 133193, vw = 149.8132),
    Candle(c = 149.32, h = 150.0, l = 148.78, n = 6759, o = 149.7, t = 1669208400000, v = 552285, vw = 149.3482),
    Candle(c = 151.09, h = 151.4296, l = 149.1, n = 80896, o = 149.32, t = 1669212000000, v = 8259366, vw = 150.4984),
    Candle(c = 151.5811, h = 151.83, l = 150.63, n = 76549, o = 151.11, t = 1669215600000, v = 8530819, vw = 151.2802),
    Candle(c = 150.1, h = 151.76, l = 149.79, n = 61204, o = 151.59, t = 1669219200000, v = 7427399, vw = 150.9813),
    Candle(c = 149.645, h = 150.42, l = 149.34, n = 43245, o = 150.09, t = 1669222800000, v = 4923819, vw = 150.0486),
    Candle(c = 150.304, h = 150.46, l = 149.53, n = 33647, o = 149.64, t = 1669226400000, v = 4713932, vw = 150.0989),
    Candle(c = 150.98, h = 151.31, l = 149.76, n = 62706, o = 150.29, t = 1669230000000, v = 7720288, vw = 150.8538),
    Candle(c = 151.14, h = 151.42, l = 150.63, n = 71506, o = 150.99, t = 1669233600000, v = 9508674, vw = 151.0309),
    Candle(c = 151.12, h = 151.16, l = 151.0327, n = 1370, o = 151.11, t = 1669237200000, v = 2296111, vw = 151.0714),
    Candle(c = 151.1601, h = 151.17, l = 151.1, n = 614, o = 151.12, t = 1669240800000, v = 36147, vw = 151.1434),
    Candle(c = 151.17, h = 151.2, l = 151.09, n = 401, o = 151.16, t = 1669244400000, v = 27333, vw = 151.1518),
    Candle(c = 151.15, h = 151.21, l = 151.11, n = 507, o = 151.17, t = 1669248000000, v = 37437, vw = 151.1548)
)

@Preview
@Composable
fun CandlestickChart(
    data: List<Candle> = CANDLESTICK_TEST_DATA,
    modifier: Modifier = Modifier.fillMaxWidth().height(200.dp),
    bodyColorPriceIncrease: Color = Color.Green,
    bodyColorPriceDecrease: Color = Color.Red,
    wickColor: Color = Color.Black
) {
    Spacer(
        modifier = modifier
            .drawWithCache {
                val greenBrush = Brush.verticalGradient(listOf(bodyColorPriceIncrease, bodyColorPriceIncrease))
                val redBrush = Brush.verticalGradient(listOf(bodyColorPriceDecrease, bodyColorPriceDecrease))
                val blackBrush = Brush.verticalGradient(listOf(wickColor, wickColor))

                val max = data.map { max(max(it.h, it.o), it.c) }.reduce { acc: Double, p: Double -> max(acc, p) }
                val min = data.map { min(min(it.l, it.o), it.c) }.reduce { acc: Double, p: Double -> min(acc, p) }

                val range = max - min

                onDrawBehind {
                    fun candleY(a: Double): Float {
                        return remap(min, max, size.height.toDouble(), 0.0, a).toFloat()
                    }
                    val candleWidth = this.size.width / data.size
                    data.forEachIndexed { index, candle ->
                        drawLine(
                            blackBrush,
                            start = Offset(x = (index + 0.5f) * candleWidth, y = candleY(candle.h)),
                            end = Offset(x = (index + 0.5f) * candleWidth, y = candleY(candle.l)),
                            cap = StrokeCap.Round,
                            strokeWidth = candleWidth * 0.15f
                        )
                        drawLine(
                            if (candle.c > candle.o) greenBrush else redBrush,
                            start = Offset(x = (index + 0.5f) * candleWidth, y = candleY(candle.o)),
                            end = Offset(x = (index + 0.5f) * candleWidth, y = candleY(candle.c)),
                            cap = StrokeCap.Round,
                            strokeWidth = candleWidth * 0.8f
                        )
                    }
                }
            }
            .fillMaxWidth()
    )
}
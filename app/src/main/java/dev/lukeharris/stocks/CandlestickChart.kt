package dev.lukeharris.stocks

import androidx.compose.animation.core.*
import androidx.compose.animation.splineBasedDecay
import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.input.pointer.*
import androidx.compose.ui.input.pointer.util.VelocityTracker
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.lukeharris.stocks.ui.theme.Cousine
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.lang.Double.max
import java.lang.Double.min
import java.text.NumberFormat
import java.util.*
import kotlin.math.PI
import kotlin.math.abs

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

suspend fun PointerInputScope.detectTransformGestures(
    panZoomLock: Boolean = false,
    consume: Boolean = true,
    onGestureStart: (PointerInputChange) -> Unit = {},
    onGesture: (
        centroid: Offset,
        pan: Offset,
        zoom: Float,
        rotation: Float,
        mainPointer: PointerInputChange,
        changes: List<PointerInputChange>
    ) -> Unit,
    onGestureEnd: (PointerInputChange) -> Unit = {},
    onSelect: (PointerInputChange) -> Unit = {},
) {
    awaitEachGesture {
        var rotation = 0f
        var zoom = 1f
        var pan = Offset.Zero
        var pastTouchSlop = false
        val touchSlop = viewConfiguration.touchSlop
        var lockedToPanZoom = false
        var selectMode = false
        var selectionTimer: Long = 0


        // Wait for at least one pointer to press down, and set first contact position
        val down: PointerInputChange = awaitFirstDown(requireUnconsumed = false)

        onGestureStart(down)

        var pointer = down
        // Main pointer is the one that is down initially
        var pointerId = down.id

        do {
            val event = awaitPointerEvent()

            // If any position change is consumed from another PointerInputChange
            val canceled =
                event.changes.any { it.isConsumed }

            if (!canceled) {

                // Get pointer that is down, if first pointer is up
                // get another and use it if other pointers are also down
                // event.changes.first() doesn't return same order
                val pointerInputChange =
                    event.changes.firstOrNull { it.id == pointerId }
                        ?: event.changes.first()

                // Next time will check same pointer with this id
                pointerId = pointerInputChange.id
                pointer = pointerInputChange

                val zoomChange = event.calculateZoom()
                val rotationChange = event.calculateRotation()
                val panChange = event.calculatePan()

                if (!selectMode && !pastTouchSlop) {
                    selectionTimer += pointer.uptimeMillis - pointer.previousUptimeMillis
                    if (selectionTimer > viewConfiguration.longPressTimeoutMillis) {
                        selectMode = true
                    }
                }

                if (selectMode) {
                    onSelect(pointer)
                }

                if (!selectMode && !pastTouchSlop) {
                    zoom *= zoomChange
                    rotation += rotationChange
                    pan += panChange


                    val centroidSize = event.calculateCentroidSize(useCurrent = false)
                    val zoomMotion = abs(1 - zoom) * centroidSize
                    val rotationMotion =
                        abs(rotation * PI.toFloat() * centroidSize / 180f)
                    val panMotion = pan.getDistance()

                    if (zoomMotion > touchSlop ||
                        rotationMotion > touchSlop ||
                        panMotion > touchSlop
                    ) {
                        pastTouchSlop = true
                        lockedToPanZoom = panZoomLock && rotationMotion < touchSlop
                    }
                }

                if (!selectMode && pastTouchSlop) {
                    val centroid = event.calculateCentroid(useCurrent = false)
                    val effectiveRotation = if (lockedToPanZoom) 0f else rotationChange
                    if (effectiveRotation != 0f ||
                        zoomChange != 1f ||
                        panChange != Offset.Zero
                    ) {
                        onGesture(
                            centroid,
                            panChange,
                            zoomChange,
                            effectiveRotation,
                            pointer,
                            event.changes
                        )
                    }

                    if (consume) {
                        event.changes.forEach {
                            if (it.positionChanged()) {
                                it.consume()
                            }
                        }
                    }
                }
            }

        } while (!canceled && event.changes.any { it.pressed })
        onGestureEnd(pointer)
    }
}

@OptIn(ExperimentalTextApi::class)
@Preview
@Composable
fun CandlestickChart(
    data: List<Candle> = CANDLESTICK_TEST_DATA,
    modifier: Modifier = Modifier.fillMaxWidth().height(200.dp),
    bodyColorPriceIncrease: Color = Color.Green,
    bodyColorPriceDecrease: Color = Color.Red,
    wickColor: Color = Color.Black
) {
    var scale by remember { mutableStateOf(1f) }
    val offset = remember { Animatable(0f) }

    var targetMin by remember { mutableStateOf(0.0) }
    var targetMax by remember { mutableStateOf(0.0) }

    val rangeAnimationSpec = tween<Float>(durationMillis = 100, easing = FastOutSlowInEasing)

    val minF by animateFloatAsState(targetMin.toFloat(), rangeAnimationSpec)
    val maxF by animateFloatAsState(targetMax.toFloat(), rangeAnimationSpec)

    val min = minF.toDouble()
    val max = maxF.toDouble()

    val textMeasurer = rememberTextMeasurer()

    val numberFormat = NumberFormat.getCurrencyInstance(Locale.US)
    numberFormat.maximumFractionDigits = 2

    var longPressOffset by remember { mutableStateOf<Float?>(null) }

    Spacer(
        modifier = modifier
            .pointerInput(Unit) {
                val decay = splineBasedDecay<Float>(this)
                val velocityTracker = VelocityTracker()

                coroutineScope {
                    while (true) {
                        detectTransformGestures(
                            onGestureStart = {
                                velocityTracker.resetTracking()
                            },
                            onGesture = { centroid, pan, zoom, _, change, _ ->
                                val oldScale = scale
                                var newScale = scale * zoom

                                if (newScale < 1f) {
                                    newScale = 1f
                                }

                                var newPosition = (offset.value + centroid.x / oldScale) -
                                        (centroid.x / newScale + pan.x / oldScale)

                                val lowerBound = 0f
                                val upperBound = size.width.toFloat() - (size.width.toFloat() / newScale)

                                offset.updateBounds(lowerBound, upperBound)

                                launch {
                                    offset.snapTo(newPosition)
                                }

                                scale = newScale

                                velocityTracker.addPosition(
                                    change.uptimeMillis,
                                    Offset(x = offset.value, y = 0f)
                                )
                            },
                            onGestureEnd = {
                                val velocity = velocityTracker.calculateVelocity().x
                                launch {
                                    offset.animateDecay(velocity, decay)
                                }
                            },
                            onSelect = { pointer ->
                                longPressOffset = pointer.position.x
                            }
                        )
                    }
                }
            }
            .drawWithCache {
                val greenBrush = Brush.verticalGradient(listOf(bodyColorPriceIncrease, bodyColorPriceIncrease))
                val redBrush = Brush.verticalGradient(listOf(bodyColorPriceDecrease, bodyColorPriceDecrease))
                val blackBrush = Brush.verticalGradient(listOf(wickColor, wickColor))

                val candleWidth = this.size.width / data.size * scale

                targetMax = data
                    .filterIndexed { index, _ -> ((index + 1) * candleWidth) - (offset.value * scale) >= 0 && (index * candleWidth) - (offset.value * scale) < size.width}
                    .map { max(max(it.h, it.o), it.c) }
                    .reduce { acc: Double, p: Double -> max(acc, p) }
                targetMin = data
                    .filterIndexed { index, _ -> ((index + 1) * candleWidth) - (offset.value * scale) >= 0 && (index * candleWidth) - (offset.value * scale) < size.width}
                    .map { min(min(it.l, it.o), it.c) }
                    .reduce { acc: Double, p: Double -> min(acc, p) }

                onDrawBehind {
                    fun candleY(a: Double): Float {
                        return remap(min, max, size.height.toDouble(), 0.0, a).toFloat()
                    }

//                    if (longPressOffset != null) {
//                        drawLine(
//                            wickColor.copy(alpha = 0.3f),
//                            start = Offset(
//                                x = longPressOffset!!,
//                                y = 0f
//                            ),
//                            end = Offset(
//                                x = longPressOffset!!,
//                                y = 100f
//                            ),
//                            cap = StrokeCap.Round,
//                            strokeWidth = 1f,
//                        )
//                    }

                    ticks(min.toFloat(), max.toFloat(), 6f).forEachIndexed { index, tick ->
                        val tickY = candleY(tick.toDouble())
                        val dashGap = if (index % 2 == 0) 0f else 10f
                        drawLine(
                            wickColor.copy(alpha = 0.3f),
                            start = Offset(
                                x = 0f,
                                y = tickY
                            ),
                            end = Offset(
                                x = size.width,
                                y = tickY
                            ),
                            cap = StrokeCap.Round,
                            strokeWidth = 1f,
                            pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, dashGap), 0f)
                        )
                    }

                    // TODO: Only render candles in view
                    data.forEachIndexed { index, candle ->
                        drawLine(
                            blackBrush,
                            start = Offset(
                                x = ((index + 0.5f) * candleWidth) - (offset.value * scale),
                                y = candleY(candle.h)
                            ),
                            end = Offset(
                                x = ((index + 0.5f) * candleWidth) - (offset.value * scale),
                                y = candleY(candle.l)
                            ),
                            cap = StrokeCap.Round,
                            strokeWidth = candleWidth * 0.15f
                        )
                        drawLine(
                            if (candle.c > candle.o) greenBrush else redBrush,
                            start = Offset(
                                x = ((index + 0.5f) * candleWidth) - (offset.value * scale),
                                y = candleY(candle.o)
                            ),
                            end = Offset(
                                x = ((index + 0.5f) * candleWidth) - (offset.value * scale),
                                y = candleY(candle.c)
                            ),
                            cap = StrokeCap.Round,
                            strokeWidth = candleWidth * 0.8f
                        )
                    }

                    ticks(min.toFloat(), max.toFloat(), 6f).forEachIndexed { index, tick ->
                        if (index % 2 == 0) {
                            val tickY = candleY(tick.toDouble())
                            val label = AnnotatedString(numberFormat.format(tick))
                            val labelStyle = TextStyle(
                                fontFamily = Cousine,
                                lineHeight = 44.sp,
                                fontSize = 12.sp,
                                letterSpacing = 0.sp,
                                fontWeight = FontWeight.Normal
                            )
                            val labelLayout = textMeasurer.measure(text = label, style = labelStyle)
                            if (tickY - labelLayout.size.height > 0) {
                                drawText(
                                    textLayoutResult = labelLayout,
                                    color = wickColor,
                                    topLeft = Offset(
                                        x = size.width - labelLayout.size.width,
                                        y = tickY - labelLayout.size.height
                                    )
                                )
                            }
                        }
                    }
                }
            }
            .fillMaxWidth()
    )
}
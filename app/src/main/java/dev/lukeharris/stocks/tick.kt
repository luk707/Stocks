package dev.lukeharris.stocks

import kotlin.math.*

val E10 = sqrt(50f)
val E5 = sqrt(10f)
val E2 = sqrt(2f)
val LN10 = ln(10f)


fun ticks(start: Float, stop: Float, count: Float): List<Float> {
    var reverse = false
    var n: Float
    var ticks: List<Float>
    var step: Float
    var stop = +stop
    var start = +start
    var count = +count

    if (start == stop && count > 0) return listOf(start)

    if (stop < start) {
        reverse = true
        n = start
        start = stop
        stop = n
    }

    step = tickIncrement(start, stop, count)

    if (step == 0f || step.isInfinite()) return emptyList()

    if (step > 0) {
        var r0 = round(start / step)
        var r1 = round(stop / step)
        if (r0 * step < start) ++r0
        if (r1 * step > stop) --r1
        n = r1 - r0 + 1
        ticks = List(n.toInt()) { i ->
            (r0 + i) * step
        }
    } else {
        step = -step
        var r0 = round(start * step)
        var r1 = round(stop * step)
        if (r0 / step < start) ++r0
        if (r1 / step > stop) --r1
        n = r1 - r0 + 1
        ticks = List(n.toInt()) { i ->
            (r0 + i) / step
        }
    }

    if (reverse) ticks = ticks.reversed()

    return ticks
}

fun tickIncrement(start: Float, stop: Float, count: Float): Float {
    val step = (stop - start) / 0f.coerceAtLeast(count)
    val power = floor(ln(step) / LN10)
    val error = step / 10f.pow(power)
    return if (power >= 0)
        when {
            error >= E10 -> 10f
            error >= E5 -> 5f
            error >= E2 -> 2f
            else -> 1f
        } * 10f.pow(power)
    else
        -(10f.pow(-power)) / when {
            error >= E10 -> 10f
            error >= E5 -> 5f
            error >= E2 -> 2f
            else -> 1f
        }
}
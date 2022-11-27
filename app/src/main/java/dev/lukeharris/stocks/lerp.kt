package dev.lukeharris.stocks

fun lerp(a: Double, b: Double, t: Double): Double {
    return (1 - t) * a + b * t
}

fun iLerp(a: Double, b: Double, v: Double): Double {
    return (v - a) / (b - a)
}

fun remap(iMin: Double, iMax: Double, oMin: Double, oMax: Double, v: Double): Double {
    return lerp(oMin, oMax, iLerp(iMin, iMax, v))
}

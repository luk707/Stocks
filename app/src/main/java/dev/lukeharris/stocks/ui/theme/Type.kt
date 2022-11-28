package dev.lukeharris.stocks.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import dev.lukeharris.stocks.R

val Poppins = FontFamily(
    Font(R.font.poppins_regular),
    Font(R.font.poppins_semibold),
    Font(R.font.poppins_bold),
)

val Cousine = FontFamily(
    Font(R.font.cousine_regular),
    Font(R.font.cousine_bold)
)

// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = Poppins,
        lineHeight = 24.sp,
        fontSize = 16.sp,
        letterSpacing = .5.sp,
        fontWeight = FontWeight.Normal
    ),
    bodyMedium = TextStyle(
        fontFamily = Poppins,
        lineHeight = 20.sp,
        fontSize = 14.sp,
        letterSpacing = .2.sp,
        fontWeight = FontWeight.Normal
    ),
    bodySmall = TextStyle(
        fontFamily = Poppins,
        lineHeight = 16.sp,
        fontSize = 12.sp,
        letterSpacing = .4.sp,
        fontWeight = FontWeight.Normal,
    ),
    displayLarge = TextStyle(
        fontFamily = Poppins,
        lineHeight = 64.sp,
        fontSize = 57.sp,
        letterSpacing = (-.2).sp,
        fontWeight = FontWeight.Normal,
    ),
    displayMedium = TextStyle(
        fontFamily = Poppins,
        lineHeight = 52.sp,
        fontSize = 45.sp,
        letterSpacing = 0.sp,
        fontWeight = FontWeight.Normal
    ),
    displaySmall = TextStyle(
        fontFamily = Poppins,
        lineHeight = 44.sp,
        fontSize = 36.sp,
        letterSpacing = 0.sp,
        fontWeight = FontWeight.Normal
    ),
    headlineLarge = TextStyle(
        fontFamily = Poppins,
        lineHeight = 40.sp,
        fontSize = 32.sp,
        letterSpacing = 0.sp,
        fontWeight = FontWeight.Normal
    ),
    headlineMedium = TextStyle(
        fontFamily = Poppins,
        lineHeight = 36.sp,
        fontSize = 28.sp,
        letterSpacing = 0.sp,
        fontWeight = FontWeight.Normal
    ),
    headlineSmall = TextStyle(
        fontFamily = Poppins,
        lineHeight = 32.sp,
        fontSize = 24.sp,
        letterSpacing = 0.sp,
        fontWeight = FontWeight.Normal
    ),
    labelLarge = TextStyle(
        fontFamily = Poppins,
        lineHeight = 20.sp,
        fontSize = 14.sp,
        letterSpacing = .1.sp,
        fontWeight = FontWeight.Medium,
    ),
    labelMedium = TextStyle(
        fontFamily = Poppins,
        lineHeight = 16.sp,
        fontSize = 12.sp,
        letterSpacing = .5.sp,
        fontWeight = FontWeight.Medium,
    ),
    labelSmall = TextStyle(
        fontFamily = Poppins,
        lineHeight = 16.sp,
        fontSize = 11.sp,
        letterSpacing = .5.sp,
        fontWeight = FontWeight.Medium
    ),
    titleLarge = TextStyle(
        fontFamily = Poppins,
        lineHeight = 28.sp,
        fontSize = 22.sp,
        letterSpacing = 0.sp,
        fontWeight = FontWeight.Normal
    ),
    titleMedium = TextStyle(
        fontFamily = Poppins,
        lineHeight = 24.sp,
        fontSize = 16.sp,
        letterSpacing = .2.sp,
        fontWeight = FontWeight.Medium
    ),
    titleSmall = TextStyle(
        fontFamily = Poppins,
        lineHeight = 20.sp,
        fontSize = 14.sp,
        letterSpacing = .1.sp,
        fontWeight = FontWeight.Medium
    )
)

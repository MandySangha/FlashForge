package week11.st8907.finalproject.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color


val NeonText = Color(0xFFFF00FF) // neon pink text

// MAIN NEON COLORS
val NeonPink = Color(0xFFFF00FF)
val NeonBlue = Color(0xFF00E5FF)
val NeonPurple = Color(0xFFB347FF)

// SURFACES + TEXT
val FlashBlack = Color(0xFF000000)
val FlashDarkGray = Color(0xFF111111)
val FlashWhite = Color(0xFFFFFFFF)
val FlashGray = Color(0xFFBBBBBB)

// LIGHT THEME
val FlashLightColors = lightColorScheme(
    primary = NeonPink,
    onPrimary = FlashBlack,
    secondary = NeonBlue,
    onSecondary = FlashBlack,
    tertiary = NeonPurple,
    onTertiary = FlashBlack,

    background = FlashBlack,
    onBackground = FlashWhite,
    surface = FlashDarkGray,
    onSurface = FlashWhite
)

// DARK THEME
val FlashDarkColors = darkColorScheme(
    primary = NeonPink,
    onPrimary = FlashBlack,
    secondary = NeonBlue,
    onSecondary = FlashBlack,
    tertiary = NeonPurple,
    onTertiary = FlashBlack,

    background = FlashBlack,
    onBackground = FlashWhite,
    surface = FlashDarkGray,
    onSurface = FlashWhite
)

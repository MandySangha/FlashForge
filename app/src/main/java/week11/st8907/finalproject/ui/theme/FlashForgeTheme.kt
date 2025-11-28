package week11.st8907.finalproject.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun FlashForgeTheme(
    darkTheme: Boolean = true,
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) FlashDarkColors else FlashLightColors

    MaterialTheme(
        colorScheme = colors,
        typography = NeonTypography,
        shapes = NeonShapes,
        content = content
    )
}

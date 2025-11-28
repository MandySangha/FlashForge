package week11.st8907.finalproject.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun Theme(content: @Composable () -> Unit) {
    FlashForgeTheme {
        content()
    }
}

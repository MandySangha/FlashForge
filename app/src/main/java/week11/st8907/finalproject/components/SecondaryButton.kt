package week11.st8907.finalproject.components

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * SecondaryButton.kt
 * -------------------------------------------------------------
 * A simple outlined button for secondary actions like navigation
 * or smaller choices. Used to keep UI consistent and organized.
 */

@Composable
fun SecondaryButton(
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier
    ) {
        Text(text)
    }
}

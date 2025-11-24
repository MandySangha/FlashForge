package week11.st8907.finalproject.components

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * PrimaryButton.kt
 * -------------------------------------------------------------
 * A reusable button used as the main call-to-action across the app.
 * Clean and consistent styling helps keep the UI uniform during Step 4.
 */

@Composable
fun PrimaryButton(
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier
    ) {
        Text(text)
    }
}

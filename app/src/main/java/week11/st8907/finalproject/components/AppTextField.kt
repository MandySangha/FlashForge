package week11.st8907.finalproject.components

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * AppTextField.kt
 * -------------------------------------------------------------
 * A simple reusable text field used for all authentication and
 * form input screens. Helps maintain consistent UI across the app.
 */


@Composable
fun AppTextField(
    value: String,
    onValueChange: (String) -> Unit,   // IMPORTANT
    label: String,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = { onValueChange(it) },   // <— gives “it”
        label = { Text(label) },
        modifier = modifier
    )
}

package week11.st8907.finalproject.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

/**
 * LoadingDialog.kt
 * -------------------------------------------------------------
 * Simple modal dialog used during Firebase operations like login,
 * registration, and password reset. Displays a circular indicator.
 */

@Composable
fun LoadingDialog() {
    Dialog(onDismissRequest = { }) {
        Surface(
            shape = MaterialTheme.shapes.medium
        ) {
            CircularProgressIndicator(
                modifier = androidx.compose.ui.Modifier.padding(24.dp)
            )
        }
    }
}

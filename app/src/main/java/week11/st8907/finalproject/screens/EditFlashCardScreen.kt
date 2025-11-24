package week11.st8907.finalproject.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import week11.st8907.finalproject.navigation.Routes

/**
 * EditFlashCardScreen.kt
 * -------------------------------------------------------------
 * A placeholder for updating an existing flashcard. Later, text
 * fields will be added here along with Firestore update logic.
 * For Step 4, we only include simple navigation actions.
 */

@Composable
fun EditFlashCardScreen(navController: NavController) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Edit Flashcard Screen",
            fontSize = 28.sp,
            modifier = Modifier.padding(bottom = 40.dp)
        )

        Button(
            onClick = { navController.navigate(Routes.FlashCardDetail) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Back to Detail")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { navController.navigate(Routes.FlashCardList) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Back to Flashcard List")
        }
    }
}

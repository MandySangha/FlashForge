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
 * FlashCardDetailScreen.kt
 * -------------------------------------------------------------
 * This screen will later display full flashcard content (front/back).
 * For Step 4, this is a simple placeholder with navigation to edit
 * the flashcard or go back to the list.
 */

@Composable
fun FlashCardDetailScreen(navController: NavController) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Flashcard Detail Screen",
            fontSize = 28.sp,
            modifier = Modifier.padding(bottom = 40.dp)
        )

        Button(
            onClick = { navController.navigate(Routes.EditFlashCard) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Edit Flashcard")
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

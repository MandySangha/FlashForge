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
 * CreateFlashCardScreen.kt
 * -------------------------------------------------------------
 * Placeholder screen for manually creating new flashcards.
 * For Step 4, this screen only includes a heading and dummy
 * navigation buttons. Full text fields and Firestore logic will
 * be implemented later in Step 5.
 */

@Composable
fun CreateFlashCardScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Create Flashcard Screen",
            fontSize = 28.sp,
            modifier = Modifier.padding(bottom = 40.dp)
        )

        Button(
            onClick = { navController.navigate(Routes.Home) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Back to Home")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { navController.navigate(Routes.FlashCardList) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Go to Flashcard List")
        }
    }
}

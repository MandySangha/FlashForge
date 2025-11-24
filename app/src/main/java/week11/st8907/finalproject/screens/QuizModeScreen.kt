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
 * QuizModeScreen.kt
 * -------------------------------------------------------------
 * Placeholder for the quiz-taking interface. Later, flashcards will
 * be converted into questions. For Step 4, we simply include a heading
 * and placeholder navigation to the quiz results page.
 */

@Composable
fun QuizModeScreen(navController: NavController) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Quiz Mode Screen",
            fontSize = 28.sp,
            modifier = Modifier.padding(bottom = 40.dp)
        )

        Button(
            onClick = { navController.navigate(Routes.QuizResult) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Finish Quiz")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { navController.navigate(Routes.Home) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Back to Home")
        }
    }
}

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
 * HomeScreen.kt
 * -------------------------------------------------------------
 * The main hub screen of the FlashForge app. From here, users can
 * navigate to all primary areas including flashcard creation,
 * scanning, studying, and quizzes. This version includes only a
 * heading and dummy navigation buttons for Step 4.
 */

@Composable
fun HomeScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Home Screen",
            fontSize = 30.sp,
            modifier = Modifier.padding(bottom = 40.dp)
        )

        Button(
            onClick = { navController.navigate(Routes.StudyMode) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Go to Study Mode")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { navController.navigate(Routes.QuizMode) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Go to Quiz Mode")
        }


        Button(
            onClick = { navController.navigate(Routes.CreateFlashCard) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Create Flashcard")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { navController.navigate(Routes.ScanFlashCard) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Scan Flashcard")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { navController.navigate(Routes.FlashCardList) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("View Flashcards")
        }
    }
}

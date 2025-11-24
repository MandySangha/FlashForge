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
 * QuizResultScreen.kt
 * -------------------------------------------------------------
 * This placeholder screen will later show quiz performance such as
 * score, accuracy, and retry options. For Step 4, only minimal UI
 * is included along with navigation buttons.
 */

@Composable
fun QuizResultScreen(navController: NavController) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = "Quiz Result Screen",
            fontSize = 28.sp,
            modifier = Modifier.padding(bottom = 40.dp)
        )

        Button(
            onClick = { navController.navigate(Routes.QuizMode) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Retry Quiz")
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

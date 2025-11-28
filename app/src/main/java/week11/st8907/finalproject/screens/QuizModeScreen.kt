package week11.st8907.finalproject.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import week11.st8907.finalproject.navigation.Routes
import week11.st8907.finalproject.ui.theme.NeonPink
import week11.st8907.finalproject.ui.theme.NeonPurple
import week11.st8907.finalproject.ui.theme.NeonText

@Composable
fun QuizModeScreen(navController: NavController) {

    // Placeholder quiz
    val question = "Which language is used for Android Jetpack Compose?"
    val choices = listOf("Java", "Kotlin", "Swift", "Dart")
    var selected by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(22.dp)
    ) {

        Text(
            "Quiz Mode",
            fontSize = 28.sp,
            color = NeonText,
            modifier = Modifier.padding(bottom = 20.dp)
        )

        Text(question, color = Color.White, fontSize = 20.sp)

        Spacer(Modifier.height(30.dp))

        choices.forEach { choice ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .border(
                        width = 2.dp,
                        color = if (selected == choice) NeonPink else NeonPurple,
                        shape = RoundedCornerShape(14.dp)
                    )
                    .background(Color(0xFF1A1A1D), RoundedCornerShape(14.dp))
                    .clickable { selected = choice }
                    .padding(16.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(choice, color = Color.White, fontSize = 18.sp)
            }
        }

        Spacer(Modifier.height(30.dp))

        // Submit Button
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp)
                .background(
                    Brush.horizontalGradient(listOf(NeonPurple, NeonPink)),
                    RoundedCornerShape(16.dp)
                )
                .clickable {
                    navController.navigate(Routes.QuizResult)
                },
            contentAlignment = Alignment.Center
        ) {
            Text("Submit", color = Color.White, fontSize = 18.sp)
        }
    }
}

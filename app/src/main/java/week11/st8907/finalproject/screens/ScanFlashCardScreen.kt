package week11.st8907.finalproject.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
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
fun ScanFlashCardScreen(navController: NavController) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(22.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // Title
        Text(
            text = "Scan Flashcard",
            fontSize = 28.sp,
            color = NeonText,
            modifier = Modifier.padding(top = 20.dp, bottom = 22.dp)
        )

        // Camera Placeholder Box
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(330.dp)
                .shadow(10.dp, RoundedCornerShape(18.dp))
                .border(
                    BorderStroke(3.dp, NeonPink),
                    RoundedCornerShape(18.dp)
                )
                .background(Color(0xFF121212), RoundedCornerShape(18.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                "Camera Preview Placeholder",
                color = Color.White.copy(alpha = 0.6f),
                fontSize = 16.sp
            )
        }

        Spacer(Modifier.height(36.dp))

        // Scan Button
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(58.dp)
                .shadow(8.dp, RoundedCornerShape(14.dp))
                .background(
                    Brush.horizontalGradient(listOf(NeonPurple, NeonPink)),
                    RoundedCornerShape(14.dp)
                )
                .padding(16.dp)
                .clickable {
                    // later: OCR → extract → flashcard
                    navController.navigate(Routes.CreateFlashCard)
                },
            contentAlignment = Alignment.Center
        ) {
            Text("Scan Now", color = Color.White, fontSize = 18.sp)
        }

        Spacer(Modifier.height(20.dp))

        TextButton(onClick = {
            navController.navigate(Routes.CreateFlashCard)
        }) {
            Text("Or Enter Manually", color = NeonPink, fontSize = 16.sp)
        }
    }
}

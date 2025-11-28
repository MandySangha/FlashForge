package week11.st8907.finalproject.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import week11.st8907.finalproject.ui.theme.*

@Composable
fun StatsScreen(navController: NavController) {

    // Placeholder data
    val totalCardsStudied = 120
    val accuracy = 85
    val streak = 5
    val xp = 2400

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(22.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            "Your Stats",
            fontSize = 28.sp,
            color = NeonText,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 20.dp)
        )

        StatCard(title = "XP Earned", value = "$xp XP")
        Spacer(Modifier.height(16.dp))

        StatCard(title = "Current Streak", value = "$streak Days")
        Spacer(Modifier.height(16.dp))

        StatCard(title = "Total Cards Studied", value = "$totalCardsStudied")
        Spacer(Modifier.height(16.dp))

        StatCard(title = "Accuracy", value = "$accuracy%")
    }
}

@Composable
fun StatCard(title: String, value: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .background(
                Brush.linearGradient(listOf(NeonPurple, NeonPink)),
                RoundedCornerShape(20.dp)
            )
            .padding(20.dp)
    ) {
        Column(
            Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center
        ) {
            Text(title, color = Color.White.copy(alpha = 0.7f), fontSize = 16.sp)
            Text(
                value,
                color = Color.White,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

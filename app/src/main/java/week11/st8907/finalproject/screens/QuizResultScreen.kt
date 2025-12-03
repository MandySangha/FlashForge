package week11.st8907.finalproject.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import week11.st8907.finalproject.navigation.Routes
import week11.st8907.finalproject.ui.theme.*

@Composable
fun QuizResultScreen(
    navController: NavController,
    score: Int,
    total: Int
) {
    var pop by remember { mutableStateOf(false) }
    val animatedScale by animateFloatAsState(if (pop) 1.05f else 1f)

    // Calculate XP
    val baseXp = total * 10
    val accuracy = if (total > 0) score.toDouble() / total else 0.0
    val bonusXp = if (accuracy > 0.8) (baseXp * 0.2).toInt() else 0
    val totalXp = baseXp + bonusXp

    LaunchedEffect(Unit) { pop = true }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(22.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            "Your Quiz Results",
            fontSize = 28.sp,
            color = NeonText,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // SCORE CARD
        Box(
            modifier = Modifier
                .scale(animatedScale)
                .fillMaxWidth()
                .height(200.dp)
                .background(
                    Brush.verticalGradient(listOf(NeonPurple, NeonPink)),
                    RoundedCornerShape(20.dp)
                )
                .padding(20.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    "$score / $total",
                    color = Color.White,
                    fontSize = 50.sp,
                    fontWeight = FontWeight.ExtraBold
                )

                Text(
                    when {
                        score == total -> "Perfect Score! 🔥"
                        score >= total * 0.7 -> "Great job! Keep going!"
                        else -> "Keep practicing, you got this!"
                    },
                    color = Color.White,
                    fontSize = 18.sp
                )

                Spacer(Modifier.height(8.dp))

                Text(
                    "Earned $totalXp XP!",
                    color = Color(0xFFFFD700), // Gold color for XP
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(Modifier.height(35.dp))

        NeonButton(
            text = "Study Again",
            onClick = { navController.navigate(Routes.StudyMode) }
        )

        Spacer(Modifier.height(16.dp))

        NeonButton(
            text = "Back to Home",
            onClick = {
                navController.navigate(Routes.Home) {
                    popUpTo(Routes.Home) { inclusive = true }
                }
            }
        )
    }
}

@Composable
fun NeonButton(text: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(55.dp)
            .background(
                Brush.horizontalGradient(listOf(NeonPurple, NeonPink)),
                RoundedCornerShape(16.dp)
            )
            .clickableNoRipple { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(text, color = Color.White, fontSize = 18.sp)
    }
}

@Composable
fun Modifier.clickableNoRipple(onClick: () -> Unit): Modifier =
    this.then(
        Modifier.clickable(
            indication = null,
            interactionSource = remember { MutableInteractionSource() }
        ) { onClick() }
    )

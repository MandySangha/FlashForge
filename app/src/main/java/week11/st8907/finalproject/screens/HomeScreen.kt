package week11.st8907.finalproject.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import week11.st8907.finalproject.navigation.Routes
import week11.st8907.finalproject.ui.theme.NeonPink
import week11.st8907.finalproject.ui.theme.NeonPurple
import week11.st8907.finalproject.ui.theme.NeonText
import week11.st8907.finalproject.data.viewmodels.FlashcardViewModel
import week11.st8907.finalproject.data.viewmodels.ProfileViewModel
import week11.st8907.finalproject.data.viewmodels.UserViewModel

@Composable
fun HomeScreen(
    navController: NavController,
    profileVM: UserViewModel = viewModel(),
    cardVM: FlashcardViewModel = viewModel()
) {
    val currentUser by profileVM.user.collectAsState()
    val userFlashcards by cardVM.userFlashcards.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.Top
    ) {

        // Profile
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "FlashForge",
                fontSize = 34.sp,
                color = NeonText,
            )

            // Profile Icon Button
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clickable { navController.navigate(Routes.Profile) }
                    .background(
                        Brush.linearGradient(listOf(NeonPurple, NeonPink)),
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                    Text(
                        text = currentUser?.name?.firstOrNull()?.toString() ?: "U",
                        color = Color.White,
                        fontSize = 20.sp
                    )
            }
        }

        Text(
            text = "Welcome back 👋 ${currentUser?.name ?: "User"}",
            fontSize = 18.sp,
            color = Color.White.copy(alpha = 0.7f)
        )

        Spacer(Modifier.height(28.dp))

        // Stats Row
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            HomeStatBox("XP", currentUser?.xp ?: 0)
            HomeStatBox("Streak", currentUser?.streak ?: 0)
            HomeStatBox("Cards", userFlashcards.size)
        }

        Spacer(Modifier.height(28.dp))

        Text("Quick Actions", color = NeonText, fontSize = 20.sp)
        Spacer(Modifier.height(16.dp))

        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            HomeActionCard("Create\nFlashcard") {
                navController.navigate(Routes.CreateFlashCard)
            }

            HomeActionCard("Scan\nFlashcard") {
                navController.navigate(Routes.ScanFlashCard)
            }
        }

        Spacer(Modifier.height(16.dp))

        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            HomeActionCard("Study\nMode") {
                navController.navigate(Routes.StudyMode)
            }

            HomeActionCard("Quiz\nMode") {
                navController.navigate(Routes.QuizMode)
            }
        }

        Spacer(Modifier.height(28.dp))

        Text("Your Flashcards", color = NeonText, fontSize = 20.sp)
        Spacer(Modifier.height(12.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(6.dp, RoundedCornerShape(14.dp))
                .background(
                    Brush.horizontalGradient(listOf(NeonPurple, NeonPink)),
                    RoundedCornerShape(14.dp)
                )
                .padding(20.dp)
                .clickable { navController.navigate(Routes.FlashCardList) }
        ) {
            Text(
                text = "View All Flashcards",
                fontSize = 18.sp,
                color = Color.White
            )
        }
    }
}

@Composable
fun HomeStatBox(title: String, value: Int) {
    Column(
        modifier = Modifier
            .width(100.dp)
            .shadow(6.dp, RoundedCornerShape(10.dp))
            .background(Color(0xFF1B1B1D), RoundedCornerShape(10.dp))
            .padding(14.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(value.toString(), fontSize = 24.sp, color = NeonText)
        Text(title, fontSize = 14.sp, color = Color.Gray)
    }
}

@Composable
fun HomeActionCard(title: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .width(160.dp)
            .height(120.dp)
            .shadow(8.dp, RoundedCornerShape(18.dp))
            .background(
                Brush.verticalGradient(
                    listOf(NeonPurple, NeonPink)
                ),
                RoundedCornerShape(18.dp)
            )
            .clickable { onClick() }
            .padding(16.dp)
    ) {
        Text(
            text = title,
            color = Color.White,
            fontSize = 18.sp,
            modifier = Modifier.align(Alignment.BottomStart)
        )
    }
}
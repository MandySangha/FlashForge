package week11.st8907.finalproject.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import week11.st8907.finalproject.viewmodel.UserViewModel
import week11.st8907.finalproject.viewmodels.FlashcardViewModel


@Composable
fun HomeScreen(
    navController: NavController,
    userVM: UserViewModel = viewModel(),
    cardVM: FlashcardViewModel = viewModel()
) {
    val user by userVM.user.collectAsState()
    val flashcards by cardVM.cards.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.Top
    ) {

        // Header
        Text(
            text = "FlashForge",
            fontSize = 34.sp,
            color = NeonText,
            modifier = Modifier.padding(top = 20.dp, bottom = 12.dp)
        )

        Text(
            text = "Welcome back 👋 ${user?.name ?: ""}",
            fontSize = 18.sp,
            color = Color.White.copy(alpha = 0.7f)
        )

        Spacer(Modifier.height(28.dp))

        // Stats Row
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            HomeStatBox("XP", user?.xp ?: 0)
            HomeStatBox("Streak", user?.streak ?: 0)
            HomeStatBox("Cards", flashcards.size)
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


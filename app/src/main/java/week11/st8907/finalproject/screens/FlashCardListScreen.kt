package week11.st8907.finalproject.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
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
import androidx.compose.runtime.collectAsState
import com.google.firebase.auth.FirebaseAuth

@Composable
fun FlashCardListScreen(
    navController: NavController,
    viewModel: FlashcardViewModel = viewModel()
) {
    println("DEBUG SCREEN: FlashCardListScreen composable called")
    println("DEBUG SCREEN: ViewModel instance: $viewModel")

    val currentUser = FirebaseAuth.getInstance().currentUser
    val userId = currentUser?.uid ?: ""

    println("DEBUG SCREEN: Current userId: '$userId'")
    println("DEBUG SCREEN: User authenticated: ${currentUser != null}")


    // Load user flashcards when screen appears
    LaunchedEffect(userId) {
        if (userId.isNotBlank()) {
            viewModel.loadUserFlashcards(userId)
        }
    }

    val flashcards by viewModel.userFlashcards.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {

        // ---------- HEADER ----------
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.Default.ArrowBack, contentDescription = null, tint = NeonText)
            }

            Spacer(Modifier.width(10.dp))

            Text(
                "Your Flashcards",
                fontSize = 30.sp,
                color = NeonText
            )
        }

        Spacer(Modifier.height(20.dp))

        // ---------- CREATE BUTTON ----------
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp)
                .shadow(8.dp, RoundedCornerShape(14.dp))
                .background(
                    Brush.horizontalGradient(listOf(NeonPurple, NeonPink)),
                    RoundedCornerShape(14.dp)
                )
                .clickable { navController.navigate(Routes.CreateFlashCard) }
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text("Create New Flashcard", color = Color.White, fontSize = 17.sp)
        }

        Spacer(Modifier.height(20.dp))

        // ---------- EMPTY STATE ----------
        if (flashcards.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("No flashcards yet!", color = Color.Gray, fontSize = 18.sp)
            }
            return
        }

        // ---------- FLASHCARD LIST ----------
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            items(flashcards) { card ->

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(6.dp, RoundedCornerShape(12.dp))
                        .background(Color(0xFF1A1A1D), RoundedCornerShape(12.dp))
                        .clickable {
                            navController.navigate("flashcard_detail/${card.cardId}")
                        }
                        .padding(18.dp)
                ) {
                    Column {
                        Text(
                            text = card.question,
                            fontSize = 17.sp,
                            color = Color.White
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = "Tap to view",
                            fontSize = 13.sp,
                            color = NeonPink
                        )
                    }
                }
            }
        }
    }
}
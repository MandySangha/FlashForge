package week11.st8907.finalproject.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
fun FlashCardDetailScreen(
    navController: NavController,
    cardId: String,
    viewModel: FlashcardViewModel = viewModel()
) {
    val currentUser = FirebaseAuth.getInstance().currentUser
    val userId = currentUser?.uid ?: ""

    // Load user flashcards when screen appears
    LaunchedEffect(userId) {
        if (userId.isNotBlank()) {
            viewModel.loadUserFlashcards(userId)
        }
    }

    val userFlashcards by viewModel.userFlashcards.collectAsState()
    val card = userFlashcards.find { it.cardId == cardId }

    var showDeleteDialog by remember { mutableStateOf(false) }

    if (card == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) { Text("Loading…", color = Color.White) }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(22.dp)
    ) {

        // Top bar
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.Default.ArrowBack, contentDescription = null, tint = NeonText)
            }
            Spacer(Modifier.width(8.dp))
            Text(
                "Flashcard Details",
                fontSize = 28.sp,
                color = NeonText
            )
        }

        Spacer(Modifier.height(26.dp))

        // Flashcard container
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(12.dp, RoundedCornerShape(20.dp))
                .background(Color(0xFF1A1A1D), RoundedCornerShape(20.dp))
                .border(2.dp, NeonPink, RoundedCornerShape(20.dp))
                .padding(20.dp)
        ) {

            Column {
                Text("Question:", color = NeonPurple, fontSize = 18.sp)
                Text(card.question, color = Color.White, fontSize = 20.sp)

                Spacer(Modifier.height(20.dp))

                Text("Answer:", color = NeonPurple, fontSize = 18.sp)
                Text(card.answer, color = Color.White, fontSize = 20.sp)
            }
        }

        Spacer(Modifier.height(40.dp))

        // Edit Button
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp)
                .background(
                    Brush.horizontalGradient(listOf(NeonPurple, NeonPink)),
                    RoundedCornerShape(14.dp)
                )
                .shadow(10.dp, RoundedCornerShape(14.dp))
                .clickable {
                    navController.navigate("edit_flashcard/${card.cardId}")
                },
            contentAlignment = Alignment.Center
        ) {
            Text("Edit Flashcard", color = Color.White, fontSize = 17.sp)
        }

        Spacer(Modifier.height(18.dp))

        // Delete Button
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp)
                .border(2.dp, Color.Red, RoundedCornerShape(14.dp))
                .clickable { showDeleteDialog = true },
            contentAlignment = Alignment.Center
        ) {
            Text("Delete Flashcard", color = Color.Red, fontSize = 16.sp)
        }
    }

    // Delete Dialog
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete Flashcard?", color = Color.Red) },
            text = { Text("Are you sure you want to delete this flashcard?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.deleteFlashcard(card.cardId, userId) {
                            showDeleteDialog = false
                            navController.navigate(Routes.FlashCardList) {
                                popUpTo(Routes.FlashCardList) { inclusive = true }
                            }
                        }
                    }
                ) { Text("Delete", color = Color.Red) }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancel", color = NeonText)
                }
            }
        )
    }
}
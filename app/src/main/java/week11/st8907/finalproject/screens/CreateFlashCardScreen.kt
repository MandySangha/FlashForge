package week11.st8907.finalproject.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import week11.st8907.finalproject.components.PrimaryButton
import week11.st8907.finalproject.data.models.Flashcard
import week11.st8907.finalproject.data.viewmodels.FlashcardViewModel
import week11.st8907.finalproject.data.viewmodels.ProfileViewModel
import week11.st8907.finalproject.ui.theme.NeonPink
import week11.st8907.finalproject.ui.theme.NeonPurple
import week11.st8907.finalproject.ui.theme.NeonText


@Composable
fun CreateFlashCardScreen(
    navController: NavController,
    flashcardViewModel: FlashcardViewModel = viewModel()
) {
    val currentUser = FirebaseAuth.getInstance().currentUser
    val userId = currentUser?.uid ?: ""

    var question by remember { mutableStateOf("") }
    var answer by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val isLoading by flashcardViewModel.isLoading.collectAsState()

    LaunchedEffect(Unit) {
        flashcardViewModel.resetLoading()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(22.dp)
    ) {
        // TOP BAR
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.Default.ArrowBack, contentDescription = null, tint = Color.White)
            }
            Spacer(Modifier.width(10.dp))
            Text("Create Flashcard", color = Color.White, fontSize = 26.sp)
        }

        Spacer(Modifier.height(20.dp))

        // Question field
        TextField(
            value = question,
            onValueChange = { question = it },
            label = { Text("Question", color = Color.LightGray) },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = NeonPink,
                unfocusedIndicatorColor = NeonPurple,
                cursorColor = NeonPink,
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            )
        )

        Spacer(Modifier.height(20.dp))

        // Answer field
        TextField(
            value = answer,
            onValueChange = { answer = it },
            label = { Text("Answer", color = Color.LightGray) },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = NeonPink,
                unfocusedIndicatorColor = NeonPurple,
                cursorColor = NeonPink,
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            )
        )

        // Error message
        errorMessage?.let {
            Spacer(Modifier.height(16.dp))
            Text(it, color = Color.Red, fontSize = 14.sp)
        }

        Spacer(Modifier.height(30.dp))

        // Save button
        PrimaryButton(
            text = if (isLoading) "Saving..." else "Save",
            onClick = {
                if (userId.isBlank()) {
                    errorMessage = "Error: User not authenticated"
                    return@PrimaryButton
                }

                if (question.isBlank() || answer.isBlank()) {
                    errorMessage = "Please fill in both question and answer"
                    return@PrimaryButton
                }

                flashcardViewModel.createFlashcard(
                    flashcard = Flashcard(
                        userId = userId,
                        question = question.trim(),
                        answer = answer.trim()
                    ),
                    onSuccess = { cardId ->
                        showDialog = true
                        errorMessage = null
                    }
                )
            },
            )
    }

    // Confirmation dialog
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Saved!", color = NeonText) },
            text = { Text("Your flashcard has been added.") },
            confirmButton = {
                TextButton(onClick = {
                    showDialog = false
                    navController.popBackStack()
                }) {
                    Text("OK", color = NeonText)
                }
            }
        )
    }
}
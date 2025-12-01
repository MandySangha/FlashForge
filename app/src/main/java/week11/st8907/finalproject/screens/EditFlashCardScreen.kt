package week11.st8907.finalproject.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import week11.st8907.finalproject.components.PrimaryButton
import week11.st8907.finalproject.data.viewmodels.FlashcardViewModel
import week11.st8907.finalproject.models.Flashcard
import week11.st8907.finalproject.ui.theme.NeonPink
import week11.st8907.finalproject.ui.theme.NeonText

@Composable
fun EditFlashCardScreen(
    navController: NavController,
    cardId: String,
    viewModel: FlashcardViewModel = viewModel()
) {
    val allCards by viewModel.userFlashcards.collectAsState()
    val card = allCards.find { it.cardId == cardId }

    var question by remember { mutableStateOf("") }
    var answer by remember { mutableStateOf("") }
    var showSavedDialog by remember { mutableStateOf(false) }

    // Load card data into fields
    LaunchedEffect(card) {
        card?.let {
            question = it.question
            answer = it.answer
        }
    }

    if (card == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) { Text("Loading…", color = NeonText) }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(22.dp)
    ) {

        // Top Bar
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.Default.ArrowBack, contentDescription = null, tint = NeonText)
            }
            Spacer(Modifier.width(8.dp))
            Text(
                "Edit Flashcard",
                fontSize = 28.sp,
                color = NeonText
            )
        }

        Spacer(Modifier.height(20.dp))

        // Question Field
        OutlinedTextField(
            value = question,
            onValueChange = { question = it },
            label = { Text("Question") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(20.dp))

        // Answer Field
        OutlinedTextField(
            value = answer,
            onValueChange = { answer = it },
            label = { Text("Answer") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(30.dp))

        // Save Button
        PrimaryButton(
            text = "Save Changes",
            onClick = {
                val updated = card.copy(
                    question = question.trim(),
                    answer = answer.trim()
                )
                viewModel.updateFlashcard(updated) {
                    showSavedDialog = true
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(12.dp))

        TextButton(onClick = { navController.popBackStack() }) {
            Text("Cancel", color = NeonPink)
        }
    }

    // Success Dialog
    if (showSavedDialog) {
        AlertDialog(
            onDismissRequest = {},
            title = { Text("Flashcard Updated") },
            text = { Text("Your changes have been saved successfully.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showSavedDialog = false
                        navController.popBackStack()
                    }
                ) {
                    Text("OK", color = NeonPink)
                }
            }
        )
    }
}

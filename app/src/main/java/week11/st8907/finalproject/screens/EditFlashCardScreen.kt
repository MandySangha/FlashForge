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
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import week11.st8907.finalproject.navigation.Routes
import week11.st8907.finalproject.ui.theme.NeonPink
import week11.st8907.finalproject.ui.theme.NeonPurple
import week11.st8907.finalproject.ui.theme.NeonText
import week11.st8907.finalproject.viewmodels.FlashcardViewModel
import week11.st8907.finalproject.models.Flashcard

@Composable
fun EditFlashCardScreen(
    navController: NavController,
    cardId: String,
    viewModel: FlashcardViewModel = viewModel()
) {
    val allCards by viewModel.cards.collectAsState()
    val card = allCards.find { it.cardId == cardId }

    var question by remember { mutableStateOf(TextFieldValue("")) }
    var answer by remember { mutableStateOf(TextFieldValue("")) }
    var showSavedDialog by remember { mutableStateOf(false) }

    // Load card data into fields
    LaunchedEffect(card) {
        if (card != null) {
            question = TextFieldValue(card.question)
            answer = TextFieldValue(card.answer)
        }
    }

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
            .padding(22.dp),
        horizontalAlignment = Alignment.Start
    ) {

        // ---------- TOP BAR ----------
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

        // ---------- QUESTION FIELD ----------
        OutlinedTextField(
            value = question,
            onValueChange = { question = it },
            label = { Text("Question") },
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, NeonPurple, RoundedCornerShape(12.dp)),
            textStyle = LocalTextStyle.current.copy(color = Color.White),
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(Modifier.height(20.dp))

        // ---------- ANSWER FIELD ----------
        OutlinedTextField(
            value = answer,
            onValueChange = { answer = it },
            label = { Text("Answer") },
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, NeonPurple, RoundedCornerShape(12.dp)),
            textStyle = LocalTextStyle.current.copy(color = Color.White),
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(Modifier.height(30.dp))

        // ---------- SAVE BUTTON ----------
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp)
                .shadow(10.dp, RoundedCornerShape(14.dp))
                .background(
                    Brush.horizontalGradient(listOf(NeonPurple, NeonPink)),
                    RoundedCornerShape(14.dp)
                )
                .clickable {
                    val updated = Flashcard(
                        cardId = card.cardId,
                        question = question.text.trim(),
                        answer = answer.text.trim(),
                        userId = card.userId,
                        createdAt = card.createdAt,
                        lastReviewed = card.lastReviewed,
                        difficulty = card.difficulty,
                        category = card.category,
                        tags = card.tags,
                        deckId = card.deckId,
                        sourceType = card.sourceType,
                        isPublic = card.isPublic
                    )
                    viewModel.updateCard(updated)
                    showSavedDialog = true
                },
            contentAlignment = Alignment.Center
        ) {
            Text("Save Changes", color = Color.White, fontSize = 17.sp)
        }

        Spacer(Modifier.height(12.dp))

        TextButton(onClick = { navController.popBackStack() }) {
            Text("Cancel", color = NeonPink)
        }
    }

    // ---------- SUCCESS DIALOG ----------
    if (showSavedDialog) {
        AlertDialog(
            onDismissRequest = {},
            title = { Text("Flashcard Updated", color = NeonPurple) },
            text = { Text("Your changes have been saved successfully.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showSavedDialog = false
                        navController.navigate("flashcard_detail/$cardId") {
                            popUpTo(Routes.FlashCardList)
                        }
                    }
                ) {
                    Text("OK", color = NeonPink)
                }
            }
        )
    }
}

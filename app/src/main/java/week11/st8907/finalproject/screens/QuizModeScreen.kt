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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import week11.st8907.finalproject.navigation.Routes
import week11.st8907.finalproject.ui.theme.NeonPink
import week11.st8907.finalproject.ui.theme.NeonPurple
import week11.st8907.finalproject.ui.theme.NeonText
import week11.st8907.finalproject.viewmodels.FlashcardViewModel

@Composable
fun QuizModeScreen(
    navController: NavController,
    viewModel: FlashcardViewModel
) {
    val cards by viewModel.cards.collectAsState()

    if (cards.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No flashcards available for quiz.", color = NeonText)
        }
        return
    }

    var index by remember { mutableStateOf(0) }
    var score by remember { mutableStateOf(0) }
    var selected by remember { mutableStateOf<String?>(null) }
    var showAnswer by remember { mutableStateOf(false) }

    val card = cards[index]

    // Generate choices (1 correct + 3 random incorrect)
    val allAnswers = cards.map { it.answer }
    val incorrect = allAnswers.filter { it != card.answer }.shuffled().take(3)
    val choices = remember(index) {
        (incorrect + card.answer).shuffled()
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

            Text(
                "Quiz Mode",
                color = NeonText,
                fontSize = 28.sp,
                modifier = Modifier.padding(start = 10.dp)
            )
        }

        Spacer(Modifier.height(10.dp))

        // Progress
        Text(
            text = "Question ${index + 1} / ${cards.size}",
            color = NeonPink,
            fontSize = 18.sp,
            fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
        )

        Spacer(Modifier.height(20.dp))

        // Question
        Text(
            text = card.question,
            color = Color.White,
            fontSize = 20.sp
        )

        Spacer(Modifier.height(30.dp))

        // Choices
        choices.forEach { choice ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .border(
                        width = 2.dp,
                        color = when {
                            !showAnswer -> (if (selected == choice) NeonPink else NeonPurple)
                            else -> {
                                if (choice == card.answer) Color.Green else Color.Red
                            }
                        },
                        shape = RoundedCornerShape(14.dp)
                    )
                    .background(Color(0xFF1A1A1D), RoundedCornerShape(14.dp))
                    .clickable(enabled = !showAnswer) {
                        selected = choice
                    }
                    .padding(16.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(choice, color = Color.White, fontSize = 18.sp)
            }
        }

        Spacer(Modifier.height(30.dp))

        // Submit / Next Button
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp)
                .background(
                    Brush.horizontalGradient(listOf(NeonPurple, NeonPink)),
                    RoundedCornerShape(16.dp)
                )
                .clickable {
                    if (!showAnswer) {
                        showAnswer = true
                        if (selected == card.answer) score++
                    } else {
                        // Move to next question
                        if (index < cards.size - 1) {
                            index++
                            selected = null
                            showAnswer = false
                        } else {
                            // Quiz finished → send score
                            navController.navigate("${Routes.QuizResult}/$score/${cards.size}")
                        }
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                if (!showAnswer) "Submit" else if (index < cards.size - 1) "Next" else "Finish",
                color = Color.White,
                fontSize = 18.sp
            )
        }
    }
}

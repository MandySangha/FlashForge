package week11.st8907.finalproject.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import week11.st8907.finalproject.ui.theme.NeonPink
import week11.st8907.finalproject.ui.theme.NeonPurple
import week11.st8907.finalproject.ui.theme.NeonText
import week11.st8907.finalproject.viewmodels.FlashcardViewModel

@Composable
fun StudyModeScreen(
    navController: NavController,
    viewModel: FlashcardViewModel
) {
    val cards by viewModel.cards.collectAsState()

    if (cards.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                "No flashcards available.",
                color = NeonText,
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
        return
    }

    var index by remember { mutableStateOf(0) }
    var flipped by remember { mutableStateOf(false) }

    val card = cards[index]

    val rotation by animateFloatAsState(if (flipped) 180f else 0f)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(22.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // TOP BAR
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.Default.ArrowBack, contentDescription = null, tint = NeonText)
            }

            Spacer(Modifier.width(10.dp))

            Text(
                "Study Mode",
                color = NeonText,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(Modifier.height(14.dp))

        // PROGRESS — e.g., "1 / 10"
        Text(
            text = "${index + 1} / ${cards.size}",
            color = NeonPink,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(20.dp))

        // FLASHCARD BOX
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(320.dp)
                .graphicsLayer {
                    rotationY = rotation
                    cameraDistance = 12f * density
                }
                .background(
                    Brush.verticalGradient(
                        listOf(Color(0xFF1A1A1D), Color(0xFF2B2B30))
                    ),
                    RoundedCornerShape(20.dp)
                )
                .clickable {
                    flipped = !flipped
                }
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            if (rotation <= 90f) {
                Text(
                    text = card.question,
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.SemiBold
                )
            } else {
                Text(
                    text = card.answer,
                    color = NeonPurple,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.graphicsLayer { rotationY = 180f }
                )
            }
        }

        Spacer(Modifier.height(30.dp))

        // NAVIGATION BUTTONS
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            // PREVIOUS
            Button(
                onClick = {
                    if (index > 0) {
                        index--
                        flipped = false
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = NeonPurple),
                shape = RoundedCornerShape(14.dp)
            ) {
                Text("Previous", color = Color.White)
            }

            // NEXT
            Button(
                onClick = {
                    if (index < cards.size - 1) {
                        index++
                        flipped = false
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = NeonPink),
                shape = RoundedCornerShape(14.dp)
            ) {
                Text("Next", color = Color.White)
            }
        }
    }
}

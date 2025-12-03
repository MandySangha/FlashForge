package week11.st8907.finalproject.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import week11.st8907.finalproject.ui.theme.NeonPink
import week11.st8907.finalproject.ui.theme.NeonPurple
import week11.st8907.finalproject.ui.theme.NeonText
import week11.st8907.finalproject.data.viewmodels.FlashcardViewModel

@Composable
fun StudyModeScreen(
    navController: NavController,
    viewModel: FlashcardViewModel = viewModel()
) {
    val currentUser = FirebaseAuth.getInstance().currentUser
    val userId = currentUser?.uid ?: ""

    // Load flashcards when screen appears
    LaunchedEffect(userId) {
        if (userId.isNotBlank()) {
            viewModel.loadUserFlashcards(userId)
        }
    }

    val cards by viewModel.userFlashcards.collectAsState()

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
    var hasAwardedDailyXP by remember { mutableStateOf(false) }

    val card = cards[index]

    val rotation by animateFloatAsState(if (flipped) 180f else 0f)

    // Handle Done button click - awards XP and navigates back
    fun handleDoneClick() {
        if (index < cards.size - 1) {
            // Go to next card
            index++
            flipped = false
        } else {
            // Reached the end - award XP
            if (!hasAwardedDailyXP && userId.isNotBlank()) {
                viewModel.awardDailyStudyXP(
                    userId = userId,
                    onSuccess = {
                        hasAwardedDailyXP = true
                        navController.popBackStack()
                    }
                )
            } else {
                navController.popBackStack()
            }
        }
    }

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
            IconButton(
                onClick = {
                    navController.popBackStack()
                }
            ) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = NeonText)
            }

            Spacer(Modifier.width(10.dp))

            Text(
                "Study Mode",
                color = NeonText,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.weight(1f))

            // Daily XP badge (only shows if not awarded yet)
            if (!hasAwardedDailyXP) {
                Badge(
                    containerColor = NeonPink,
                    contentColor = Color.White
                ) {
                    Text("+20 XP", fontSize = 12.sp)
                }
            }
        }

        Spacer(Modifier.height(14.dp))

        // PROGRESS
        Text(
            text = "${index + 1} / ${cards.size}",
            color = NeonPink,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(20.dp))

        // FLASHCARD
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
                enabled = index > 0,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (index > 0) NeonPurple else Color.Gray
                ),
                shape = RoundedCornerShape(14.dp)
            ) {
                Text("Previous", color = Color.White)
            }

            // NEXT / DONE
            Button(
                onClick = { handleDoneClick() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (index < cards.size - 1) NeonPink else Color.Green
                ),
                shape = RoundedCornerShape(14.dp)
            ) {
                Text(
                    text = if (index < cards.size - 1) "Next" else "Done",
                    color = Color.White
                )
            }
        }

        Spacer(Modifier.height(20.dp))

        // SIMPLE INSTRUCTION
        Text(
            text = "Tap card to flip • Click 'Done' after studying",
            color = Color.LightGray,
            fontSize = 14.sp
        )
    }
}
package week11.st8907.finalproject.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import week11.st8907.finalproject.components.PrimaryButton
import week11.st8907.finalproject.data.models.Flashcard
import week11.st8907.finalproject.data.viewmodels.FlashcardViewModel
import week11.st8907.finalproject.viewmodel.OCRViewModel
import week11.st8907.finalproject.ui.theme.NeonPink
import week11.st8907.finalproject.ui.theme.NeonPurple

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateFlashCardScreen(
    navController: NavController,
    flashcardViewModel: FlashcardViewModel,
    ocrViewModel: OCRViewModel
) {
    val scannedText = ocrViewModel.recognizedText.collectAsState().value
    val currentUser = FirebaseAuth.getInstance().currentUser
    val userId = currentUser?.uid ?: ""

    var question by remember { mutableStateOf(scannedText) }
    var answer by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("General") }
    var categoryExpanded by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val categories = listOf(
        "General",
        "Math",
        "Science",
        "History",
        "Geography",
        "Language",
        "Programming",
        "Medicine",
        "Law",
        "Business",
        "Art",
        "Music",
        "Sports",
        "Personal"
    )

    LaunchedEffect(scannedText) {
        if (scannedText.isNotBlank()) {
            question = scannedText
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(22.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, tint = Color.White)
            }
            Text("Create Flashcard", color = Color.White, fontSize = 26.sp)
        }

        Spacer(Modifier.height(20.dp))

        // Question Field
        TextField(
            value = question,
            onValueChange = { question = it },
            label = { Text("Question") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = NeonPink,
                unfocusedIndicatorColor = NeonPurple,
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            )
        )

        Spacer(Modifier.height(20.dp))

        // Answer Field
        TextField(
            value = answer,
            onValueChange = { answer = it },
            label = { Text("Answer") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = NeonPink,
                unfocusedIndicatorColor = NeonPurple,
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            )
        )

        Spacer(Modifier.height(20.dp))

        // Category Dropdown
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            ExposedDropdownMenuBox(
                expanded = categoryExpanded,
                onExpandedChange = { categoryExpanded = !categoryExpanded }
            ) {
                TextField(
                    value = selectedCategory,
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoryExpanded)
                    },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth(),
                    label = { Text("Category") },
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = NeonPink,
                        unfocusedIndicatorColor = NeonPurple,
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    )
                )

                ExposedDropdownMenu(
                    expanded = categoryExpanded,
                    onDismissRequest = { categoryExpanded = false }
                ) {
                    categories.forEach { category ->
                        DropdownMenuItem(
                            text = { Text(category, color = Color.White) },
                            onClick = {
                                selectedCategory = category
                                categoryExpanded = false
                            },
                            modifier = Modifier.background(Color(0xFF1A1A1D))
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(30.dp))

        // Error Message
        errorMessage?.let { message ->
            Text(
                text = message,
                color = Color.Red,
                fontSize = 14.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )
        }

        // Save Button
        PrimaryButton(
            text = "Save",
            onClick = {
                if (question.isBlank() || answer.isBlank()) {
                    errorMessage = "Please fill in both fields"
                    return@PrimaryButton
                }

                flashcardViewModel.createFlashcard(
                    flashcard = Flashcard(
                        userId = userId,
                        question = question,
                        answer = answer,
                        category = selectedCategory
                    ),
                    onSuccess = {
                        ocrViewModel.clearRecognizedText()
                        showDialog = true
                    }
                )
            }
        )
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Saved!") },
            text = { Text("Flashcard added") },
            confirmButton = {
                TextButton(onClick = {
                    showDialog = false
                    navController.popBackStack()
                }) {
                    Text("OK")
                }
            }
        )
    }
}
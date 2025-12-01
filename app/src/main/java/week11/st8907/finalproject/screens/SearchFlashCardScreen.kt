package week11.st8907.finalproject.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import week11.st8907.finalproject.data.viewmodels.FlashcardViewModel
import week11.st8907.finalproject.data.models.Flashcard
import androidx.compose.runtime.collectAsState
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SearchFlashCardScreen(
    navController: NavController,
    viewModel: FlashcardViewModel = viewModel()
) {
    val currentUser = FirebaseAuth.getInstance().currentUser
    val userId = currentUser?.uid ?: ""
    val coroutineScope = rememberCoroutineScope()

    var query by remember { mutableStateOf("") }
    var results by remember { mutableStateOf<List<Flashcard>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }
    var searchJob by remember { mutableStateOf<Job?>(null) }

    // Get all user flashcards first
    LaunchedEffect(userId) {
        if (userId.isNotBlank()) {
            viewModel.loadUserFlashcards(userId)
        }
    }

    val allFlashcards by viewModel.userFlashcards.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(20.dp)
    ) {

        Text(
            text = "Search Flashcards",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFB388FF), // Neon Purple
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Search Bar
        TextField(
            value = query,
            onValueChange = { newQuery ->
                query = newQuery

                // Cancel previous search job
                searchJob?.cancel()

                // Debounce search
                if (newQuery.isNotBlank()) {
                    isLoading = true
                    searchJob = coroutineScope.launch {
                        delay(300) // Debounce for 300ms
                        viewModel.searchFlashcards(userId, newQuery) { searchResults ->
                            results = searchResults
                            isLoading = false
                        }
                    }
                } else {
                    results = emptyList()
                    isLoading = false
                }
            },
            label = { Text("Search flashcards…", color = Color(0xFFB388FF)) },
            trailingIcon = {
                Icon(Icons.Default.Search, contentDescription = null, tint = Color(0xFF82B1FF))
            },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedIndicatorColor = Color(0xFFB388FF),
                unfocusedIndicatorColor = Color(0xFF82B1FF),
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedLabelColor = Color(0xFFB388FF),
                unfocusedLabelColor = Color(0xFF82B1FF)
            )
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Loading indicator
        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color(0xFFB388FF))
            }
        }
        // Results
        else if (results.isEmpty() && query.isNotBlank()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("No results found", color = Color.Gray)
            }
        } else if (results.isEmpty() && query.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Start typing to search...", color = Color.Gray)
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(results) { card ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .clickable {
                                navController.navigate("flashcard_detail/${card.cardId}")
                            },
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A1A))
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = card.question,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF82B1FF) // Neon Blue
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = card.answer,
                                color = Color.LightGray,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }
        }
    }
}
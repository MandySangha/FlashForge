package week11.st8907.finalproject.screens

import androidx.compose.foundation.background
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
import androidx.navigation.NavController
import androidx.lifecycle.viewmodel.compose.viewModel
import week11.st8907.finalproject.navigation.Routes
import week11.st8907.finalproject.viewmodels.FlashcardViewModel
import week11.st8907.finalproject.models.Flashcard

@Composable
fun SearchFlashCardScreen(
    navController: NavController,
    viewModel: FlashcardViewModel

) {
    var query by remember { mutableStateOf("") }
    var results by remember { mutableStateOf(listOf<Flashcard>()) }

    val neonPurple = Color(0xFFB388FF)
    val neonBlue = Color(0xFF82B1FF)
    val neonBlack = Color(0xFF0A0A0A)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(neonBlack)
            .padding(20.dp)
    ) {

        Text(
            text = "Search Flashcards",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = neonPurple,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Search Bar
        OutlinedTextField(
            value = query,
            onValueChange = {
                query = it

                // 🔥 Correct search call
                results = viewModel.searchFlashcards(query)
            },
            label = { Text("Search flashcards…", color = neonPurple) },
            trailingIcon = {
                Icon(Icons.Default.Search, contentDescription = null, tint = neonBlue)
            },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedBorderColor = neonPurple,
                unfocusedBorderColor = neonBlue,
            )
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Results
        if (results.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("No results found", color = Color.Gray)
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(results) { card ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A1A))
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {

                            Text(
                                text = card.question,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = neonBlue
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = card.answer,
                                color = Color.LightGray,
                                fontSize = 14.sp
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            Button(
                                onClick = {
                                    navController.navigate(Routes.FlashCardDetail)
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = neonPurple
                                )
                            ) {
                                Text("View Details")
                            }
                        }
                    }
                }
            }
        }
    }
}

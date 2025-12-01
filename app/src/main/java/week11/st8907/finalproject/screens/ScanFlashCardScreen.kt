package week11.st8907.finalproject.screens

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import week11.st8907.finalproject.components.CameraPreview
import week11.st8907.finalproject.navigation.Routes
import week11.st8907.finalproject.ui.theme.NeonPink
import week11.st8907.finalproject.ui.theme.NeonPurple
import week11.st8907.finalproject.ui.theme.NeonText

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScanFlashCardScreen(navController: NavController) {
    var capturedBitmap by remember { mutableStateOf<android.graphics.Bitmap?>(null) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Scan Flashcard",
                        color = NeonText,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = { navController.popBackStack() }
                    ) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = NeonText
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.Black
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(22.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Camera Preview Upgrade
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(330.dp)
                    .shadow(10.dp, RoundedCornerShape(18.dp))
                    .border(BorderStroke(3.dp, NeonPink), RoundedCornerShape(18.dp))
            ) {
                CameraPreview(
                    modifier = Modifier.fillMaxSize(),
                    onPhotoCaptured = { bitmap ->
                        capturedBitmap = bitmap
                        // TODO: send to OCR
                        navController.navigate(Routes.CreateFlashCard)
                    }
                )
            }

            Spacer(Modifier.height(36.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(58.dp)
                    .shadow(8.dp, RoundedCornerShape(14.dp))
                    .background(
                        Brush.horizontalGradient(listOf(NeonPurple, NeonPink)),
                        RoundedCornerShape(14.dp)
                    )
                    .padding(16.dp)
                    .clickable {
                        // Trigger capture by tapping preview
                    },
                contentAlignment = Alignment.Center
            ) {
                Text("Tap Preview to Scan", color = Color.White, fontSize = 18.sp)
            }

            Spacer(Modifier.height(20.dp))

            TextButton(onClick = {
                navController.navigate(Routes.CreateFlashCard)
            }) {
                Text("Or Enter Manually", color = NeonPink, fontSize = 16.sp)
            }
        }
    }
}
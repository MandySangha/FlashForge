package week11.st8907.finalproject.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import week11.st8907.finalproject.components.PrimaryButton
import week11.st8907.finalproject.components.SecondaryButton
import week11.st8907.finalproject.data.viewmodels.ProfileViewModel
import week11.st8907.finalproject.ui.theme.NeonPink
import week11.st8907.finalproject.ui.theme.NeonPurple
import week11.st8907.finalproject.ui.theme.NeonText


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(navController: NavController) {
    val profileViewModel: ProfileViewModel = viewModel()

    // Load user data when screen appears
    LaunchedEffect(Unit) {
        profileViewModel.loadUserProfile()
    }

    val currentUser by profileViewModel.currentUser.collectAsState()
    val isLoading by profileViewModel.isLoading.collectAsState()
    val errorMessage by profileViewModel.errorMessage.collectAsState()

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }

    // Update form when user data loads
    LaunchedEffect(currentUser) {
        name = currentUser?.name ?: ""
        email = currentUser?.email ?: ""
    }

    // Validate form
    val isFormValid = name.isNotBlank() && email.isNotBlank() && email.contains("@")
    val hasChanges = name != currentUser?.name || email != currentUser?.email

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Edit Profile",
                        color = NeonText,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = NeonText
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.Black)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Name Field
            TextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Name", color = Color.LightGray) },
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = NeonPink,
                    unfocusedIndicatorColor = NeonPurple,
                    cursorColor = NeonPink,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedLabelColor = NeonPink,
                    unfocusedLabelColor = Color.LightGray
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(20.dp))

            // Email Field
            TextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email", color = Color.LightGray) },
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = NeonPink,
                    unfocusedIndicatorColor = NeonPurple,
                    cursorColor = NeonPink,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedLabelColor = NeonPink,
                    unfocusedLabelColor = Color.LightGray
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(32.dp))

            // Save Button
            PrimaryButton(
                text = if (isLoading) "Saving..." else "Save Changes",
                onClick = {
                    profileViewModel.updateUserProfile(
                        name = name.trim(),
                        email = email.trim(),
                        onSuccess = { navController.popBackStack() }
                    )
                },
            )

            Spacer(Modifier.height(16.dp))

            // Cancel Button
            SecondaryButton(
                text = "Cancel",
                onClick = { navController.popBackStack() },
            )

            // Error Message
            errorMessage?.let { message ->
                Spacer(Modifier.height(16.dp))
                Text(
                    text = message,
                    color = Color.Red,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
            }
        }
    }
}
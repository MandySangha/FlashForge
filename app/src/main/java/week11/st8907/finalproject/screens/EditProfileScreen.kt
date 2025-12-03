package week11.st8907.finalproject.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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
import android.widget.Toast
import androidx.compose.material.icons.automirrored.filled.ArrowBack


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(navController: NavController) {
    val profileViewModel: ProfileViewModel = viewModel()
    val context = LocalContext.current

    // Load user data when screen appears
    LaunchedEffect(Unit) {
        profileViewModel.loadUserProfile()
    }

    val currentUser by profileViewModel.currentUser.collectAsState()
    val isLoading by profileViewModel.isLoading.collectAsState()
    val errorMessage by profileViewModel.errorMessage.collectAsState()

    var name by remember { mutableStateOf("") }

    // Update form when user data loads
    LaunchedEffect(currentUser) {
        name = currentUser?.name ?: ""
    }

    // Validate form - only name is required
    val isFormValid = name.isNotBlank()
    val hasChanges = name != currentUser?.name

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
                            Icons.AutoMirrored.Filled.ArrowBack,
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
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
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

            Spacer(Modifier.height(32.dp))

            // Save Button
            PrimaryButton(
                text = if (isLoading) "Saving..." else "Save Changes",
                onClick = {
                    if (isFormValid && hasChanges) {
                        profileViewModel.updateUserProfile(
                            name = name.trim(),
                            onSuccess = {
                                Toast.makeText(context, "Profile updated", Toast.LENGTH_SHORT)
                                    .show()
                                navController.popBackStack()
                            }
                        )
                    }
                }
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
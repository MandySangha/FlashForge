package week11.st8907.finalproject.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import week11.st8907.finalproject.navigation.Routes
import week11.st8907.finalproject.auth.AuthViewModel
import week11.st8907.finalproject.auth.AuthState

/**
 * LoginScreen.kt
 * -------------------------------------------------------------
 * Authentication screen placeholder with MVVM structure.
 * For Step 4c, we only inject the AuthViewModel and observe
 * authState. Firebase logic will be implemented in Step 4a.
 */

@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: AuthViewModel = AuthViewModel() // simple injection for Step 4
) {
    val authState by viewModel.authState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = "Login Screen",
            fontSize = 28.sp,
            modifier = Modifier.padding(bottom = 40.dp)
        )

        // Observing state (Step 4c only)
        when (authState) {
            is AuthState.Loading -> Text("Loading...")
            is AuthState.Error -> Text("Error occurred")
            else -> {}
        }

        Button(
            onClick = { navController.navigate(Routes.Home) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Go to Home")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { navController.navigate(Routes.Register) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Go to Register")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { navController.navigate(Routes.ForgotPassword) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Forgot Password")
        }
    }
}

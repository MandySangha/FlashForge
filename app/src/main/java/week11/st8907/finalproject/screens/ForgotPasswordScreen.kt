package week11.st8907.finalproject.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import week11.st8907.finalproject.auth.AuthState
import week11.st8907.finalproject.auth.AuthViewModel
import week11.st8907.finalproject.navigation.Routes

/**
 * ForgotPasswordScreen.kt
 * -------------------------------------------------------------
 * Password reset placeholder with ViewModel integration. StateFlow
 * observation is added for Step 4c, and Firebase reset logic will
 * be added in Step 4a.
 */

@Composable
fun ForgotPasswordScreen(
    navController: NavController,
    viewModel: AuthViewModel = AuthViewModel()
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
            text = "Forgot Password Screen",
            fontSize = 28.sp,
            modifier = Modifier.padding(bottom = 40.dp)
        )

        when (authState) {
            is AuthState.Loading -> Text("Sending reset email...")
            is AuthState.Error -> Text("Reset Error")
            else -> {}
        }

        Button(
            onClick = { navController.navigate(Routes.Login) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Back to Login")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { navController.navigate(Routes.Register) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Go to Register")
        }
    }
}

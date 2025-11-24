package week11.st8907.finalproject.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import week11.st8907.finalproject.auth.AuthViewModel
import week11.st8907.finalproject.auth.AuthState
import week11.st8907.finalproject.navigation.Routes

/**
 * RegisterScreen.kt
 * -------------------------------------------------------------
 * Registration placeholder with MVVM wiring. The ViewModel is injected
 * and authState is observed, preparing for Firebase in Step 4a.
 */

@Composable
fun RegisterScreen(
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
            text = "Register Screen",
            fontSize = 28.sp,
            modifier = Modifier.padding(bottom = 40.dp)
        )

        when (authState) {
            is AuthState.Loading -> Text("Loading...")
            is AuthState.Error -> Text("Registration Error")
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
            onClick = { navController.navigate(Routes.ForgotPassword) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Forgot Password")
        }
    }
}

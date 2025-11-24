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
import week11.st8907.finalproject.components.LoadingDialog
import week11.st8907.finalproject.components.PrimaryButton
import week11.st8907.finalproject.components.SecondaryButton
import week11.st8907.finalproject.navigation.Routes
import week11.st8907.finalproject.components.AppTextField


/**
 * ForgotPasswordScreen.kt
 * -------------------------------------------------------------
 * Allows users to request a password reset email. Uses AuthViewModel
 * and observes state via StateFlow. Firebase logic is already handled
 * in the AuthRepository.
 */

@Composable
fun ForgotPasswordScreen(
    navController: NavController,
    viewModel: AuthViewModel = AuthViewModel()
) {
    val authState by viewModel.authState.collectAsState()

    var email by remember { mutableStateOf("") }

    // Navigate back to Login when reset email is sent
    if (authState is AuthState.Success) {
        LaunchedEffect(true) {
            navController.navigate(Routes.Login)
            viewModel.resetState()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = "Reset Password",
            fontSize = 28.sp,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        AppTextField(
            value = email,
            onValueChange = { email = it },      // FIXED
            label = "Email",
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))

        PrimaryButton(
            text = "Send Reset Email",
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                viewModel.resetPassword(email)
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        SecondaryButton(
            text = "Back to Login",
            modifier = Modifier.fillMaxWidth(),
            onClick = { navController.navigate(Routes.Login) }
        )
    }

    // Loading Dialog
    if (authState is AuthState.Loading) {
        LoadingDialog()
    }

    // Error Text
    if (authState is AuthState.Error) {
        Text(
            text = (authState as AuthState.Error).error,
            color = MaterialTheme.colorScheme.error,
            modifier = Modifier.padding(top = 16.dp)
        )
    }
}

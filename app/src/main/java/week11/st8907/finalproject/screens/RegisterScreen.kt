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
import week11.st8907.finalproject.components.AppTextField
import week11.st8907.finalproject.components.LoadingDialog
import week11.st8907.finalproject.components.PrimaryButton
import week11.st8907.finalproject.components.SecondaryButton
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

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    if (authState is AuthState.Success) {
        LaunchedEffect(true) {
            navController.navigate(Routes.Login) {
                popUpTo(Routes.Register) { inclusive = true }
            }
            viewModel.resetState()
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text("Register", fontSize = 28.sp)

        Spacer(Modifier.height(16.dp))

        AppTextField(
            value = email,
            onValueChange = { email = it },
            label = "Email",
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(16.dp))

        AppTextField(
            value = password,
            onValueChange = { password = it },
            label = "Password",
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(20.dp))

        PrimaryButton(
            text = "Create Account",
            modifier = Modifier.fillMaxWidth(),
            onClick = { viewModel.register(email, password) }
        )

        Spacer(Modifier.height(12.dp))

        SecondaryButton(
            text = "Back to Login",
            modifier = Modifier.fillMaxWidth(),
            onClick = { navController.navigate(Routes.Login) }
        )
    }

    if (authState is AuthState.Loading) LoadingDialog()
    if (authState is AuthState.Error) {
        Text(
            text = (authState as AuthState.Error).error,
            color = MaterialTheme.colorScheme.error
        )
    }
}

package week11.st8907.finalproject.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import week11.st8907.finalproject.auth.AuthState
import week11.st8907.finalproject.auth.AuthViewModel
import week11.st8907.finalproject.components.AppTextField
import week11.st8907.finalproject.components.PrimaryButton
import week11.st8907.finalproject.components.SecondaryButton
import week11.st8907.finalproject.navigation.Routes
import week11.st8907.finalproject.ui.theme.NeonText

@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: AuthViewModel = viewModel()
) {
    val context = LocalContext.current

    val email by viewModel.email.collectAsState()
    val password by viewModel.password.collectAsState()
    val authState by viewModel.authState.collectAsState()

    // React to login success/error
    LaunchedEffect(authState) {
        when (authState) {

            is AuthState.Success -> {
                // Wait for Firebase to update currentUser
                kotlinx.coroutines.delay(500)

                val user = com.google.firebase.auth.FirebaseAuth.getInstance().currentUser
                if (user != null) {
                    Toast.makeText(context, "Login Successful", Toast.LENGTH_SHORT).show()

                    viewModel.clearState()

                    navController.navigate(Routes.Home) {
                        popUpTo(Routes.Login) { inclusive = true }
                    }
                } else {
                    Toast.makeText(context, "Firebase not ready, retrying…", Toast.LENGTH_SHORT).show()
                }
            }

            is AuthState.Error -> {
                Toast.makeText(
                    context,
                    (authState as AuthState.Error).message,
                    Toast.LENGTH_LONG
                ).show()
                viewModel.clearState()
            }

            else -> {}
        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(28.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text("Welcome to FlashForge", fontSize = 32.sp, color = NeonText)

        Spacer(Modifier.height(20.dp))

        AppTextField(
            value = email,
            onValueChange = { viewModel.updateEmail(it) },
            label = "Email"
        )

        Spacer(Modifier.height(16.dp))

        AppTextField(
            value = password,
            onValueChange = { viewModel.updatePassword(it) },
            label = "Password",
            isPassword = true
        )

        Spacer(Modifier.height(20.dp))

        PrimaryButton(
            text = "Login",
            onClick = { viewModel.login() }
        )

        Spacer(Modifier.height(12.dp))

        SecondaryButton(
            text = "Create Account",
            onClick = { navController.navigate(Routes.Register) }
        )

        Spacer(Modifier.height(6.dp))

        SecondaryButton(
            text = "Forgot Password?",
            onClick = { navController.navigate(Routes.ForgotPassword) }
        )
    }
}

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
fun ForgotPasswordScreen(
    navController: NavController,
    viewModel: AuthViewModel = viewModel()
) {
    val context = LocalContext.current
    val email by viewModel.email.collectAsState()
    val authState by viewModel.authState.collectAsState()

    LaunchedEffect(authState) {
        when (authState) {
            is AuthState.Success -> {
                Toast.makeText(context, "Reset email sent!", Toast.LENGTH_SHORT).show()
                viewModel.clearState()
                navController.navigate(Routes.Login)
            }
            is AuthState.Error -> {
                Toast.makeText(context, (authState as AuthState.Error).message, Toast.LENGTH_LONG).show()
                viewModel.clearState()
            }
            else -> {}
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(28.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text("Reset Password", fontSize = 30.sp, color = NeonText)

        Spacer(Modifier.height(24.dp))

        AppTextField(
            value = email,
            onValueChange = { viewModel.updateEmail(it) },
            label = "Enter your Email"
        )

        Spacer(Modifier.height(24.dp))

        PrimaryButton(
            text = "Send Reset Email",
            onClick = { viewModel.resetPassword() }
        )

        Spacer(Modifier.height(12.dp))

        SecondaryButton(
            text = "Back to Login",
            onClick = { navController.navigate(Routes.Login) }
        )
    }
}

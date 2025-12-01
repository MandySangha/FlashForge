package week11.st8907.finalproject.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
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
import week11.st8907.finalproject.components.LoadingDialog
import week11.st8907.finalproject.components.PrimaryButton
import week11.st8907.finalproject.components.SecondaryButton
import week11.st8907.finalproject.navigation.Routes

@Composable
fun RegisterScreen(
    navController: NavController,
    viewModel: AuthViewModel = viewModel()
) {
    val context = LocalContext.current

    val authState by viewModel.authState.collectAsState()
    val email by viewModel.email.collectAsState()
    val password by viewModel.password.collectAsState()
    val confirmPassword by viewModel.confirmPassword.collectAsState()

    // React to register result
    LaunchedEffect(authState) {
        when (authState) {
            is AuthState.Success -> {
                Toast.makeText(context, "Account created!", Toast.LENGTH_SHORT).show()
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
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text("Create Account", fontSize = 28.sp)

        Spacer(Modifier.height(16.dp))

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

        Spacer(Modifier.height(16.dp))

        AppTextField(
            value = confirmPassword,
            onValueChange = { viewModel.updateConfirmPassword(it) },
            label = "Confirm Password",
            isPassword = true
        )

        Spacer(Modifier.height(20.dp))

        PrimaryButton(
            text = "Register",
            onClick = {
                if (password == confirmPassword && password.isNotBlank())
                    viewModel.register()
                else
                    Toast.makeText(context, "Passwords do not match", Toast.LENGTH_SHORT).show()
            }
        )

        Spacer(Modifier.height(12.dp))

        SecondaryButton(
            text = "Back to Login",
            onClick = { navController.navigate(Routes.Login) }
        )
    }

    if (authState is AuthState.Loading) LoadingDialog()
}

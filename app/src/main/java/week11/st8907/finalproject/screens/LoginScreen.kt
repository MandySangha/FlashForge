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



import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await

class AuthRepository(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
) {

    suspend fun login(email: String, password: String): AuthState {
        return try {
            auth.signInWithEmailAndPassword(email, password).await()
            AuthState.Success("Login Successful")
        } catch (e: Exception) {
            AuthState.Error(e.message ?: "Login Failed")
        }
    }

    suspend fun register(email: String, password: String): AuthState {
        return try {
            auth.createUserWithEmailAndPassword(email, password).await()
            AuthState.Success("Registration Successful")
        } catch (e: Exception) {
            AuthState.Error(e.message ?: "Registration Failed")
        }
    }

    suspend fun resetPassword(email: String): AuthState {
        return try {
            auth.sendPasswordResetEmail(email).await()
            AuthState.Success("Password reset email sent")
        } catch (e: Exception) {
            AuthState.Error(e.message ?: "Reset Failed")
        }
    }
}

package week11.st8907.finalproject.auth

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
            AuthState.Success("Reset Email Sent")
        } catch (e: Exception) {
            AuthState.Error(e.message ?: "Reset Failed")
        }
    }
}

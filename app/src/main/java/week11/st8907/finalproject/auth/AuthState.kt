package week11.st8907.finalproject.auth

/**
 * AuthState.kt
 * -------------------------------------------------------------
 * Represents different UI states during authentication flows.
 * Used by the AuthViewModel to communicate with the UI using
 * StateFlow. These states will be updated once Firebase logic
 * is implemented.
 */

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    data class Success(val message: String = "") : AuthState()
    data class Error(val error: String) : AuthState()
}

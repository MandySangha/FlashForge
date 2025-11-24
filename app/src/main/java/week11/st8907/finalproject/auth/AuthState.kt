package week11.st8907.finalproject.auth

/**
 * AuthState.kt
 * -------------------------
 * Holds the UI state for all authentication screens.
 * This state is observed by Compose using StateFlow.
 */

data class AuthState(
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val success: Boolean = false
)

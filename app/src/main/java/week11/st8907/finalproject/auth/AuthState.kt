package week11.st8907.finalproject.auth

/**
 * AuthState.kt
 * -------------------------
 * Holds the UI state for all authentication screens.
 * This state is observed by Compose using StateFlow.
 */



sealed class AuthState {
    data object Loading : AuthState()

    data class Success(val message: String) : AuthState()

    data class Error(val message: String) : AuthState()

    data object Idle : AuthState()
}


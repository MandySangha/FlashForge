package week11.st8907.finalproject.auth

/**
 * AuthResult.kt
 * -------------------------
 * A simple wrapper used by the Repository and ViewModel
 * to represent Loading, Success, and Error states.
 */

sealed class AuthResult<out T> {
    object Loading : AuthResult<Nothing>()
    data class Success<T>(val data: T) : AuthResult<T>()
    data class Error(val message: String) : AuthResult<Nothing>()
}

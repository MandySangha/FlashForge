package week11.st8907.finalproject.auth

/**
 * AuthRepository.kt
 * -------------------------------------------------------------
 * This repository will hold all authentication operations.
 * For Step 4c, we only define method placeholders. Firebase
 * implementations will be added in Step 4a.
 */

class AuthRepository {

    suspend fun login(email: String, password: String): AuthState {
        return AuthState.Idle // Placeholder for Step 4
    }

    suspend fun register(email: String, password: String): AuthState {
        return AuthState.Idle // Placeholder for Step 4
    }

    suspend fun resetPassword(email: String): AuthState {
        return AuthState.Idle // Placeholder for Step 4
    }
}

package week11.st8907.finalproject.auth

/**
 * AuthRepository.kt
 * -------------------------
 * Repository layer for authentication.
 * Step 4c only requires the structure, not Firebase logic.
 */

class AuthRepository {

    suspend fun login(email: String, password: String): AuthResult<Unit> {
        // Firebase logic will be added in Step 4a
        return AuthResult.Loading
    }

    suspend fun register(email: String, password: String): AuthResult<Unit> {
        // Firebase logic will be added in Step 4a
        return AuthResult.Loading
    }

    suspend fun resetPassword(email: String): AuthResult<Unit> {
        // Firebase logic will be added in Step 4a
        return AuthResult.Loading
    }
}

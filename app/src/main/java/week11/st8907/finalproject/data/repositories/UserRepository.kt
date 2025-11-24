/**
 * UserRepository.kt
 * -------------------------
 * Repository for user profile management.
 * Handles user data, progress tracking, XP, and streak calculations.
 */
package week11.st8907.finalproject.data.repositories

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.tasks.await
import week11.st8907.finalproject.data.models.User
import java.util.Date

class UserRepository {
    private val firestore = FirebaseFirestore.getInstance()
    private val usersCollection = firestore.collection("users")

    // CREATE/UPDATE - Create or update user profile
    suspend fun createOrUpdateUser(user: User): Result<Boolean> {
        return try {
            usersCollection.document(user.userId).set(user).await()
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // READ - Get user profile
    suspend fun getUser(userId: String): Result<User> {
        return try {
            val document = usersCollection.document(userId).get().await()
            if (document.exists()) {
                val user = document.toObject<User>()
                if (user != null) {
                    Result.success(user)
                } else {
                    Result.failure(Exception("User data is null"))
                }
            } else {
                // Create default user profile if doesn't exist
                val defaultUser = User(userId = userId)
                usersCollection.document(userId).set(defaultUser).await()
                Result.success(defaultUser)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // UPDATE - Update user XP and streak
    suspend fun updateUserProgress(userId: String, xpEarned: Int): Result<Boolean> {
        return try {
            val userResult = getUser(userId)
            if (userResult.isSuccess) {
                val user = userResult.getOrThrow()
                val today = Date()
                val lastStudyDate = user.lastStudyDate

                // Calculate streak
                val newStreak = if (isConsecutiveDay(lastStudyDate, today)) {
                    user.streak + 1
                } else {
                    1
                }

                val updates = mapOf(
                    "xp" to user.xp + xpEarned,
                    "streak" to newStreak,
                    "lastStudyDate" to today,
                    "totalStudySessions" to user.totalStudySessions + 1
                )

                usersCollection.document(userId).update(updates).await()
                Result.success(true)
            } else {
                Result.failure(userResult.exceptionOrNull() ?: Exception("User not found"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun isConsecutiveDay(lastDate: Date?, currentDate: Date): Boolean {
        if (lastDate == null) return false

        val calendar = java.util.Calendar.getInstance()
        calendar.time = lastDate
        val lastDay = calendar.get(java.util.Calendar.DAY_OF_YEAR)
        val lastYear = calendar.get(java.util.Calendar.YEAR)

        calendar.time = currentDate
        val currentDay = calendar.get(java.util.Calendar.DAY_OF_YEAR)
        val currentYear = calendar.get(java.util.Calendar.YEAR)

        return (currentYear == lastYear && currentDay == lastDay + 1) ||
                (currentYear == lastYear + 1 && currentDay == 1 && lastDay == 365)
    }
}
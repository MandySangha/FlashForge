package week11.st8907.finalproject.data.repositories

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.tasks.await
import week11.st8907.finalproject.data.models.User

class UserRepository {

    private val firestore = FirebaseFirestore.getInstance()
    private val usersCollection = firestore.collection("users")

    // ---------------------------------------------------------------------
    // CREATE or UPDATE USER
    // ---------------------------------------------------------------------
    suspend fun createOrUpdateUser(user: User): Result<Boolean> {
        return try {
            val docRef = usersCollection.document(user.userId)
            val existing = docRef.get().await()

            val finalUser = if (existing.exists()) {
                val oldUser = existing.toObject<User>() ?: user

                oldUser.copy(
                    name = if (user.name.isNotBlank()) user.name else oldUser.name,
                    email = if (user.email.isNotBlank()) user.email else oldUser.email,
                )

            } else {
                user
            }

            docRef.set(finalUser).await()
            Result.success(true)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    // ---------------------------------------------------------------------
    // GET USER PROFILE
    // ---------------------------------------------------------------------
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
                // Create a default user if none exists
                val defaultUser = User(
                    userId = userId,
                )
                usersCollection.document(userId).set(defaultUser).await()
                Result.success(defaultUser)
            }

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // ---------------------------------------------------------------------
    // GET USER REALTIME LISTENER
    // ---------------------------------------------------------------------
    fun getUserRealtime(userId: String, listener: (User?) -> Unit) {
        usersCollection.document(userId)
            .addSnapshotListener { snapshot, _ ->
                listener(snapshot?.toObject<User>())
            }
    }

    // ---------------------------------------------------------------------
    // UPDATE XP, STREAK, AND PROGRESS
    // ---------------------------------------------------------------------
    suspend fun updateUserProgress(userId: String, xpEarned: Int): Result<Boolean> {
        return try {
            val result = getUser(userId)

            if (!result.isSuccess) {
                return Result.failure(result.exceptionOrNull() ?: Exception("User not found"))
            }

            val user = result.getOrThrow()
            val now = System.currentTimeMillis()

            // Calculate streak
            val newStreak = if (isConsecutiveDay(user.lastStudyDate, now)) {
                user.streak + 1
            } else {
                1
            }

            val updates = mapOf(
                "xp" to user.xp + xpEarned,
                "streak" to newStreak,
                "lastStudyDate" to now,
                "totalStudySessions" to user.totalStudySessions + 1
            )

            usersCollection.document(userId).update(updates).await()
            Result.success(true)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // ---------------------------------------------------------------------
    // STREAK CALCULATION USING LONG timestamps (not Date)
    // ---------------------------------------------------------------------
    private fun isConsecutiveDay(lastTimestamp: Long?, currentTimestamp: Long): Boolean {
        if (lastTimestamp == null) return false

        val oneDayMillis = 24 * 60 * 60 * 1000
        val diff = currentTimestamp - lastTimestamp

        return diff in oneDayMillis..(oneDayMillis * 2)
    }
}

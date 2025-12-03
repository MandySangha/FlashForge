package week11.st8907.finalproject.data.repositories

import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Source
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.tasks.await
import week11.st8907.finalproject.data.models.User

class UserRepository {

    private val firestore = FirebaseFirestore.getInstance()
    private val usersCollection = firestore.collection("users")

    // ---------------------------------------------------------------------
    // CREATE USER IF NOT EXISTS
    // ---------------------------------------------------------------------
    suspend fun createUserIfNotExists(user: User): Result<Boolean> {
        return try {
            val docRef = usersCollection.document(user.userId)
            val existing = docRef.get().await()

            if (!existing.exists()) {
                // create new user with defaults
                val data = hashMapOf<String, Any>(
                    "userId" to user.userId,
                    "name" to user.name,
                    "email" to user.email,
                    "xp" to 0L,
                    "streak" to 0L,
                    "totalCardsCreated" to 0L,
                    "totalStudySessions" to 0L,
                    "createdAt" to System.currentTimeMillis(),
                    "lastStudyDate" to FieldValue.delete()
                )

                docRef.set(data).await()
                println("Created new user: ${user.userId} with name: ${user.name}")
                Result.success(true)
            } else {
                // User already exists - do nothing, preserve existing data
                val existingName = existing.getString("name") ?: "Unknown"
                println("User ${user.userId} already exists. Keeping name: $existingName")
                Result.success(false)
            }

        } catch (e: Exception) {
            println("Error creating user: ${e.message}")
            Result.failure(e)
        }
    }

    // ---------------------------------------------------------------------
    // UPDATE USER PROFILE
    // ---------------------------------------------------------------------
    suspend fun updateUserProfile(userId: String, updates: Map<String, Any>): Result<Boolean> {
        return try {
            if (updates.isEmpty()) {
                return Result.success(true)
            }

            // Only update the fields provided
            usersCollection.document(userId).update(updates).await()
            println("Updated user $userId with fields: ${updates.keys}")
            Result.success(true)

        } catch (e: Exception) {
            println("Error updating user: ${e.message}")
            Result.failure(e)
        }
    }

    suspend fun updateUserName(userId: String, newName: String): Result<Boolean> {
        return updateUserProfile(userId, mapOf("name" to newName))
    }

    // ---------------------------------------------------------------------
    // GET USER PROFILE
    // ---------------------------------------------------------------------
    suspend fun getUser(userId: String): Result<User> {
        return try {
            val document = usersCollection.document(userId)
                .get(Source.SERVER)
                .await()

            if (document.exists()) {
                val user = document.toObject<User>()
                if (user != null) {
                    val userWithId = user.copy(userId = document.id)
                    Result.success(userWithId)
                } else {
                    Result.failure(Exception("User data is null"))
                }
            } else {
                val defaultUser = User(userId = userId)
                usersCollection.document(userId).set(defaultUser).await()
                Result.success(defaultUser)
            }
        } catch (e: Exception) {
            // Fallback to cache if server fails
            try {
                val document = usersCollection.document(userId).get().await()
                if (document.exists()) {
                    val user = document.toObject<User>()
                    if (user != null) {
                        val userWithId = user.copy(userId = document.id)
                        return Result.success(userWithId)
                    }
                }
            } catch (cacheEx: Exception) {
                println("ERROR UserRepo.getUser cache fallback: ${cacheEx.message}")
            }
            Result.failure(e)
        }
    }

    // ---------------------------------------------------------------------
    // GET USER REALTIME LISTENER (unchanged)
    // ---------------------------------------------------------------------
    fun getUserRealtime(userId: String, listener: (User?) -> Unit) {
        usersCollection.document(userId)
            .addSnapshotListener { snapshot, _ ->
                val user = snapshot?.toObject<User>()
                val userWithId = user?.copy(userId = snapshot.id)
                listener(userWithId)
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

            val updates = mapOf<String, Any>(
                "xp" to (user.xp + xpEarned),
                "streak" to newStreak,
                "lastStudyDate" to now,
                "totalStudySessions" to (user.totalStudySessions + 1)
            )

            updateUserProfile(userId, updates)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // ---------------------------------------------------------------------
    // STREAK CALCULATION USING LONG
    // ---------------------------------------------------------------------
    private fun isConsecutiveDay(lastTimestamp: Long?, currentTimestamp: Long): Boolean {
        if (lastTimestamp == null) return false

        val oneDayMillis = 24 * 60 * 60 * 1000
        val diff = currentTimestamp - lastTimestamp

        return diff in oneDayMillis..(oneDayMillis * 2)
    }

    // ---------------------------------------------------------------------
    // UPDATE CARD COUNT
    // ---------------------------------------------------------------------
    suspend fun updateTotalCardsCreated(userId: String): Result<Boolean> {
        return try {
            val result = getUser(userId)
            if (result.isSuccess) {
                val user = result.getOrThrow()
                val updates = mapOf<String, Any>(
                    "totalCardsCreated" to (user.totalCardsCreated + 1)
                )
                updateUserProfile(userId, updates)
            } else {
                Result.failure(result.exceptionOrNull() ?: Exception("User not found"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // ---------------------------------------------------------------------
    // USER UPDATE
    // ---------------------------------------------------------------------
    suspend fun updateUserComplete(user: User): Result<Boolean> {
        return try {
            val updates = hashMapOf<String, Any>()

            // Add all user fields
            if (user.name.isNotBlank()) updates["name"] = user.name
            if (user.email.isNotBlank()) updates["email"] = user.email
            updates["xp"] = user.xp
            updates["streak"] = user.streak
            updates["totalCardsCreated"] = user.totalCardsCreated
            updates["totalStudySessions"] = user.totalStudySessions
            updates["lastStudyDate"] = (user.lastStudyDate ?: FieldValue.delete())
            updates["createdAt"] = user.createdAt

            updateUserProfile(user.userId, updates)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
/**
 * StudySessionRepository.kt
 * -------------------------
 * Repository for study session tracking and analytics.
 * Handles recording study sessions, retrieving session history, and progress calculations.
 * Integrates with Firestore for persistent storage and real-time updates.
 */
package week11.st8907.finalproject.data.repositories

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import week11.st8907.finalproject.data.models.StudySession
import week11.st8907.finalproject.data.models.StudyProgress
import java.util.Date

class StudySessionRepository {
    private val firestore = FirebaseFirestore.getInstance()
    private val studySessionsCollection = firestore.collection("studySessions")
    private val studyProgressCollection = firestore.collection("studyProgress")

    // CREATE - Record a new study session
    suspend fun recordStudySession(session: StudySession): Result<String> {
        return try {
            val documentRef = studySessionsCollection.document()
            val newSession = session.copy(sessionId = documentRef.id, date = Date())
            documentRef.set(newSession).await()

            // Update user progress
            updateUserProgress(newSession)

            Result.success(documentRef.id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // READ - Get all study sessions for a user
    fun getUserStudySessions(userId: String): Flow<List<StudySession>> = callbackFlow {
        val listener = studySessionsCollection
            .whereEqualTo("userId", userId)
            .orderBy("date", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(emptyList())
                    return@addSnapshotListener
                }

                val sessions = snapshot?.documents?.mapNotNull { document ->
                    document.toObject<StudySession>()
                } ?: emptyList()

                trySend(sessions)
            }

        awaitClose {
            listener.remove()
        }
    }

    // READ - Get study sessions for a specific deck
    fun getDeckStudySessions(userId: String, deckId: String): Flow<List<StudySession>> = callbackFlow {
        val listener = studySessionsCollection
            .whereEqualTo("userId", userId)
            .whereEqualTo("deckId", deckId)
            .orderBy("date", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(emptyList())
                    return@addSnapshotListener
                }

                val sessions = snapshot?.documents?.mapNotNull { document ->
                    document.toObject<StudySession>()
                } ?: emptyList()

                trySend(sessions)
            }

        awaitClose {
            listener.remove()
        }
    }

    // READ - Get user study progress
    suspend fun getUserProgress(userId: String): Result<StudyProgress> {
        return try {
            val document = studyProgressCollection.document(userId).get().await()
            if (document.exists()) {
                val progress = document.toObject<StudyProgress>()
                if (progress != null) {
                    Result.success(progress)
                } else {
                    Result.failure(Exception("Progress data is null"))
                }
            } else {
                // Create default progress if doesn't exist
                val defaultProgress = StudyProgress(userId = userId)
                studyProgressCollection.document(userId).set(defaultProgress).await()
                Result.success(defaultProgress)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // READ - Get recent sessions (last 7 days)
    suspend fun getRecentSessions(userId: String, days: Int = 7): Result<List<StudySession>> {
        return try {
            val calendar = java.util.Calendar.getInstance()
            calendar.add(java.util.Calendar.DAY_OF_YEAR, -days)
            val startDate = calendar.time

            val snapshot = studySessionsCollection
                .whereEqualTo("userId", userId)
                .whereGreaterThanOrEqualTo("date", startDate)
                .orderBy("date", com.google.firebase.firestore.Query.Direction.DESCENDING)
                .get()
                .await()

            val sessions = snapshot.documents.mapNotNull { it.toObject<StudySession>() }
            Result.success(sessions)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // UPDATE - Update user progress with new session
    private suspend fun updateUserProgress(session: StudySession) {
        try {
            val progressResult = getUserProgress(session.userId)
            if (progressResult.isSuccess) {
                val currentProgress = progressResult.getOrThrow()
                val updatedProgress = currentProgress.updateWithSession(session)
                studyProgressCollection.document(session.userId).set(updatedProgress).await()
            }
        } catch (e: Exception) {
            println("Failed to update user progress: ${e.message}")
        }
    }

    // ANALYTICS - Get study statistics
    suspend fun getStudyStatistics(userId: String): Result<Map<String, Any>> {
        return try {
            val progressResult = getUserProgress(userId)
            val recentSessionsResult = getRecentSessions(userId, 30) // Last 30 days

            if (progressResult.isSuccess && recentSessionsResult.isSuccess) {
                val progress = progressResult.getOrThrow()
                val recentSessions = recentSessionsResult.getOrThrow()

                val stats = mapOf<String, Any>(
                    "totalStudyTime" to progress.getTotalStudyTimeInMinutes(),
                    "totalSessions" to progress.totalSessions,
                    "overallAccuracy" to progress.getOverallAccuracy(),
                    "currentStreak" to progress.currentStreak,
                    "recentSessionsCount" to recentSessions.size,
                    "averageSessionTime" to if (recentSessions.isNotEmpty()) {
                        recentSessions.map { it.duration }.average().toInt()
                    } else { 0 },
                    "favoriteCategory" to progress.favoriteCategory
                )

                Result.success(stats)
            } else {
                Result.failure(Exception("Failed to load progress data"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Get today's study sessions
    suspend fun getTodaySessions(userId: String): Result<List<StudySession>> {
        return try {
            val calendar = java.util.Calendar.getInstance()
            calendar.set(java.util.Calendar.HOUR_OF_DAY, 0)
            calendar.set(java.util.Calendar.MINUTE, 0)
            calendar.set(java.util.Calendar.SECOND, 0)
            calendar.set(java.util.Calendar.MILLISECOND, 0)
            val startOfDay = calendar.time

            calendar.add(java.util.Calendar.DAY_OF_YEAR, 1)
            val endOfDay = calendar.time

            val snapshot = studySessionsCollection
                .whereEqualTo("userId", userId)
                .whereGreaterThanOrEqualTo("date", startOfDay)
                .whereLessThan("date", endOfDay)
                .orderBy("date", com.google.firebase.firestore.Query.Direction.DESCENDING)
                .get()
                .await()

            val sessions = snapshot.documents.mapNotNull { it.toObject<StudySession>() }
            Result.success(sessions)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Get study streak information
    suspend fun getStudyStreak(userId: String): Result<Int> {
        return try {
            val recentSessionsResult = getRecentSessions(userId, 30)
            if (recentSessionsResult.isSuccess) {
                val sessions = recentSessionsResult.getOrThrow()
                val streak = calculateStreak(sessions)
                Result.success(streak)
            } else {
                Result.success(0)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Helper function to calculate streak
    private fun calculateStreak(sessions: List<StudySession>): Int {
        if (sessions.isEmpty()) return 0

        val calendar = java.util.Calendar.getInstance()
        val today = calendar.time

        // Sort sessions by date (newest first)
        val sortedSessions = sessions.sortedByDescending { it.date }

        var streak = 0
        var currentDate = today

        for (session in sortedSessions) {
            if (isSameDay(session.date, currentDate)) {
                // Session from chosen day
                streak++
                calendar.time = currentDate
                calendar.add(java.util.Calendar.DAY_OF_YEAR, -1)
                currentDate = calendar.time
            } else {
                break
            }
        }

        return streak
    }

    // Helper function to check if two dates are the same day
    private fun isSameDay(date1: Date, date2: Date): Boolean {
        val cal1 = java.util.Calendar.getInstance().apply { time = date1 }
        val cal2 = java.util.Calendar.getInstance().apply { time = date2 }

        return cal1.get(java.util.Calendar.YEAR) == cal2.get(java.util.Calendar.YEAR) &&
                cal1.get(java.util.Calendar.DAY_OF_YEAR) == cal2.get(java.util.Calendar.DAY_OF_YEAR)
    }
}
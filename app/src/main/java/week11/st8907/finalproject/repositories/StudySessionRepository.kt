package week11.st8907.finalproject.repositories

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import week11.st8907.finalproject.models.StudyProgress
import week11.st8907.finalproject.models.StudySession
import java.util.Date

class StudySessionRepository {

    private val firestore = FirebaseFirestore.getInstance()
    private val sessionCol = firestore.collection("studySessions")
    private val progressCol = firestore.collection("studyProgress")

    suspend fun recordStudySession(session: StudySession): Result<String> {
        return try {
            val doc = sessionCol.document()
            val updated = session.copy(sessionId = doc.id, date = Date())
            doc.set(updated).await()
            updateUserProgress(updated)
            Result.success(doc.id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getUserStudySessions(userId: String): Flow<List<StudySession>> = callbackFlow {
        val listener = sessionCol
            .whereEqualTo("userId", userId)
            .orderBy("date")
            .addSnapshotListener { snap, err ->
                if (err != null) { trySend(emptyList()); return@addSnapshotListener }
                trySend(snap?.documents?.mapNotNull { it.toObject<StudySession>() } ?: emptyList())
            }
        awaitClose { listener.remove() }
    }

    suspend fun getUserProgress(userId: String): Result<StudyProgress> {
        return try {
            val doc = progressCol.document(userId).get().await()
            val progress = doc.toObject<StudyProgress>()
            if (progress != null) Result.success(progress)
            else {
                val default = StudyProgress(userId = userId)
                progressCol.document(userId).set(default).await()
                Result.success(default)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateUserProgress(session: StudySession) {
        try {
            val curr = getUserProgress(session.userId).getOrNull() ?: StudyProgress(userId = session.userId)
            val updated = curr.updateWithSession(session)
            progressCol.document(session.userId).set(updated).await()
        } catch (_: Exception) { }
    }
}

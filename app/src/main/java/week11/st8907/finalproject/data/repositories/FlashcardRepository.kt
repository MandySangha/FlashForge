/**
 * FlashcardRepository.kt
 * -------------------------
 * Repository for flashcard CRUD operations.
 * Handles Firestore interactions for creating, reading, updating, and deleting flashcards.
 * Uses coroutines and Flow for real-time updates.
 */
package week11.st8907.finalproject.data.repositories

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import week11.st8907.finalproject.data.models.Flashcard
import java.util.Date

class FlashcardRepository {
    private val firestore = FirebaseFirestore.getInstance()
    private val flashcardsCollection = firestore.collection("flashcards")

    // CREATE - Add new flashcard
    suspend fun createFlashcard(flashcard: Flashcard): Result<String> {
        return try {
            val documentRef = flashcardsCollection.document()
            val newFlashcard = flashcard.copy(
                cardId = documentRef.id,
                createdAt = Date()
            )
            documentRef.set(newFlashcard).await()
            Result.success(documentRef.id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // READ - Get all flashcards for current user (real-time updates)
    fun getUserFlashcards(userId: String): Flow<List<Flashcard>> = callbackFlow {
        val listener = flashcardsCollection
            .whereEqualTo("userId", userId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(emptyList())
                    return@addSnapshotListener
                }

                val flashcards = snapshot?.documents?.mapNotNull { document ->
                    document.toObject<Flashcard>()
                } ?: emptyList()

                trySend(flashcards)
            }

        awaitClose {
            listener.remove()
        }
    }

    // READ - Get flashcards by category
    fun getFlashcardsByCategory(userId: String, category: String): Flow<List<Flashcard>> = callbackFlow {
        val listener = flashcardsCollection
            .whereEqualTo("userId", userId)
            .whereEqualTo("category", category)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(emptyList())
                    return@addSnapshotListener
                }

                val flashcards = snapshot?.documents?.mapNotNull { document ->
                    document.toObject<Flashcard>()
                } ?: emptyList()

                trySend(flashcards)
            }

        awaitClose {
            listener.remove()
        }
    }

    // READ - Get single flashcard
    suspend fun getFlashcard(cardId: String): Result<Flashcard> {
        return try {
            val document = flashcardsCollection.document(cardId).get().await()
            if (document.exists()) {
                val flashcard = document.toObject<Flashcard>()
                if (flashcard != null) {
                    Result.success(flashcard)
                } else {
                    Result.failure(Exception("Flashcard data is null"))
                }
            } else {
                Result.failure(Exception("Flashcard not found"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // UPDATE - Update flashcard
    suspend fun updateFlashcard(flashcard: Flashcard): Result<Boolean> {
        return try {
            flashcardsCollection.document(flashcard.cardId)
                .set(flashcard.copy(lastReviewed = Date()))
                .await()
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // DELETE - Delete flashcard
    suspend fun deleteFlashcard(cardId: String): Result<Boolean> {
        return try {
            flashcardsCollection.document(cardId).delete().await()
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // SEARCH - Search flashcards by question text
    suspend fun searchFlashcards(userId: String, query: String): Result<List<Flashcard>> {
        return try {
            val snapshot = flashcardsCollection
                .whereEqualTo("userId", userId)
                .get()
                .await()

            val flashcards = snapshot.documents.mapNotNull { it.toObject<Flashcard>() }
                .filter { flashcard ->
                    flashcard.question.contains(query, ignoreCase = true) ||
                            flashcard.answer.contains(query, ignoreCase = true) ||
                            flashcard.category.contains(query, ignoreCase = true)
                }

            Result.success(flashcards)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
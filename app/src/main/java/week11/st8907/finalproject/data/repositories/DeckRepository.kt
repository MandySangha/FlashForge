/**
 * DeckRepository.kt
 * -------------------------
 * Repository for deck management operations.
 * Handles Firestore interactions for creating, reading, updating, and deleting decks.
 * Manages deck-flashcard relationships and card counts.
 */
package week11.st8907.finalproject.data.repositories

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import week11.st8907.finalproject.data.models.Deck
import java.util.Date

class DeckRepository {
    private val firestore = FirebaseFirestore.getInstance()
    private val decksCollection = firestore.collection("decks")
    private val flashcardsCollection = firestore.collection("flashcards")

    // CREATE - Create new deck
    suspend fun createDeck(deck: Deck): Result<String> {
        return try {
            val documentRef = decksCollection.document()
            val newDeck = deck.copy(deckId = documentRef.id, createdAt = Date())
            documentRef.set(newDeck).await()
            Result.success(documentRef.id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // READ - Get all decks for user (real-time updates)
    fun getUserDecks(userId: String): Flow<List<Deck>> = callbackFlow {
        val listener = decksCollection
            .whereEqualTo("userId", userId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(emptyList())
                    return@addSnapshotListener
                }

                val decks = snapshot?.documents?.mapNotNull { document ->
                    document.toObject<Deck>()
                } ?: emptyList()

                trySend(decks)
            }

        awaitClose {
            listener.remove()
        }
    }

    // READ - Get decks by category
    fun getDecksByCategory(userId: String, category: String): Flow<List<Deck>> = callbackFlow {
        val listener = decksCollection
            .whereEqualTo("userId", userId)
            .whereEqualTo("category", category)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(emptyList())
                    return@addSnapshotListener
                }

                val decks = snapshot?.documents?.mapNotNull { document ->
                    document.toObject<Deck>()
                } ?: emptyList()

                trySend(decks)
            }

        awaitClose {
            listener.remove()
        }
    }

    // READ - Get single deck by ID
    suspend fun getDeck(deckId: String): Result<Deck> {
        return try {
            val document = decksCollection.document(deckId).get().await()
            if (document.exists()) {
                val deck = document.toObject<Deck>()
                if (deck != null) {
                    Result.success(deck)
                } else {
                    Result.failure(Exception("Deck data is null"))
                }
            } else {
                Result.failure(Exception("Deck not found"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // UPDATE - Update deck
    suspend fun updateDeck(deck: Deck): Result<Boolean> {
        return try {
            decksCollection.document(deck.deckId).set(deck).await()
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // DELETE - Delete deck
    suspend fun deleteDeck(deckId: String): Result<Boolean> {
        return try {
            decksCollection.document(deckId).delete().await()
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // SEARCH - Search decks by title or description
    suspend fun searchDecks(userId: String, query: String): Result<List<Deck>> {
        return try {
            val snapshot = decksCollection
                .whereEqualTo("userId", userId)
                .get()
                .await()

            val decks = snapshot.documents.mapNotNull { it.toObject<Deck>() }
                .filter { deck ->
                    deck.title.contains(query, ignoreCase = true) ||
                            deck.description.contains(query, ignoreCase = true) ||
                            deck.category.contains(query, ignoreCase = true)
                }

            Result.success(decks)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
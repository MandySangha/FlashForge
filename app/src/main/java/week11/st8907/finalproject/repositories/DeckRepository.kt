package week11.st8907.finalproject.repositories

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import week11.st8907.finalproject.models.Deck
import java.util.Date

class DeckRepository {

    private val firestore = FirebaseFirestore.getInstance()
    private val decksCollection = firestore.collection("decks")

    suspend fun createDeck(deck: Deck): Result<String> {
        return try {
            val document = decksCollection.document()
            val newDeck = deck.copy(deckId = document.id, createdAt = Date())
            document.set(newDeck).await()
            Result.success(document.id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getUserDecks(userId: String): Flow<List<Deck>> = callbackFlow {
        val listener = decksCollection
            .whereEqualTo("userId", userId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(emptyList())
                    return@addSnapshotListener
                }
                val decks = snapshot?.documents?.mapNotNull { it.toObject<Deck>() } ?: emptyList()
                trySend(decks)
            }
        awaitClose { listener.remove() }
    }

    suspend fun getDeck(deckId: String): Result<Deck> {
        return try {
            val doc = decksCollection.document(deckId).get().await()
            val deck = doc.toObject<Deck>()
            if (deck != null) Result.success(deck)
            else Result.failure(Exception("Deck data is null"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateDeck(deck: Deck): Result<Boolean> {
        return try {
            decksCollection.document(deck.deckId).set(deck).await()
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteDeck(deckId: String): Result<Boolean> {
        return try {
            decksCollection.document(deckId).delete().await()
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun searchDecks(userId: String, query: String): Result<List<Deck>> {
        return try {
            val snapshot = decksCollection.whereEqualTo("userId", userId).get().await()
            val decks = snapshot.documents.mapNotNull { it.toObject<Deck>() }
                .filter { d ->
                    d.title.contains(query, true) ||
                            d.description.contains(query, true) ||
                            d.category.contains(query, true)
                }
            Result.success(decks)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

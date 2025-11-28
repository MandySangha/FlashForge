package week11.st8907.finalproject.repositories

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import week11.st8907.finalproject.models.Flashcard

class FlashcardRepository(
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance(),
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
) {

    // Ensure the user document exists
    private suspend fun ensureUserDocument() {
        val userId = auth.currentUser?.uid ?: return
        val userDoc = db.collection("users").document(userId)

        val snapshot = userDoc.get().await()
        if (!snapshot.exists()) {
            userDoc.set(
                mapOf(
                    "createdAt" to System.currentTimeMillis(),
                    "uid" to userId
                )
            ).await()
        }
    }

    // Safely get the user's flashcard collection
    private fun userFlashcardsCollection(): CollectionReference? {
        val userId = auth.currentUser?.uid ?: return null
        return db.collection("users")
            .document(userId)
            .collection("flashcards")
    }

    // Add new flashcard
    suspend fun addFlashcard(card: Flashcard): Boolean {
        return try {
            ensureUserDocument()
            val col = userFlashcardsCollection() ?: return false

            val doc = col.document()
            val newCard = card.copy(cardId = doc.id)

            doc.set(newCard).await()
            true

        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    // Update a flashcard
    suspend fun updateFlashcard(card: Flashcard): Boolean {
        return try {
            ensureUserDocument()
            val col = userFlashcardsCollection() ?: return false

            col.document(card.cardId).set(card).await()
            true

        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    // Delete a flashcard
    suspend fun deleteFlashcard(cardId: String): Boolean {
        return try {
            ensureUserDocument()
            val col = userFlashcardsCollection() ?: return false

            col.document(cardId).delete().await()
            true

        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    // Realtime listener for flashcards
    fun getFlashcardsRealtime(listener: (List<Flashcard>) -> Unit) {
        val col = userFlashcardsCollection()

        if (col == null) {
            listener(emptyList())
            return
        }

        col.addSnapshotListener { snapshot, error ->
            if (error != null) {
                listener(emptyList())
                return@addSnapshotListener
            }

            val cards = snapshot?.toObjects(Flashcard::class.java) ?: emptyList()
            listener(cards)
        }
    }
}

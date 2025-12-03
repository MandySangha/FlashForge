/**
 * Flashcard.kt
 * -------------------------
 * Data model for flashcards.
 * Represents individual study cards with questions, answers, and metadata.
 */
package week11.st8907.finalproject.data.models

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp

data class Flashcard(
    @DocumentId
    val cardId: String = "",
    val question: String = "",
    val answer: String = "",
    val category: String = "General",
    val tags: List<String> = emptyList(),
    val userId: String = "",
    val deckId: String? = null,
    @ServerTimestamp
    val createdAt: Timestamp? = null,
    @ServerTimestamp
    val lastReviewed: Timestamp? = null,
    val difficulty: String = "medium", // easy, medium, hard
    val sourceType: String = "manual", // manual, ocr, voice
    val isPublic: Boolean = false
)
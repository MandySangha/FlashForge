/**
 * Deck.kt
 * -------------------------
 * Data model for flashcard decks.
 * Organizes flashcards into collections with categories and metadata.
 */

package week11.st8907.finalproject.data.models

import java.util.Date

data class Deck(
    val deckId: String = "",
    val title: String = "",
    val description: String = "",
    val category: String = "General",
    val userId: String = "",
    val createdAt: Date = Date(),
    val cardCount: Int = 0,
    val isPublic: Boolean = false
)
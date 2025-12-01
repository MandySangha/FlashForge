package week11.st8907.finalproject.models

import java.util.Date

data class Flashcard(
    val cardId: String = "",
    val question: String = "",
    val answer: String = "",
    val category: String = "General",
    val tags: List<String> = emptyList(),
    val userId: String = "",
    val deckId: String? = null,
    val createdAt: Date = Date(),
    val lastReviewed: Date? = null,
    val difficulty: String = "medium",  // easy, medium, hard
    val sourceType: String = "manual",  // manual, ocr, voice
    val isPublic: Boolean = false
)

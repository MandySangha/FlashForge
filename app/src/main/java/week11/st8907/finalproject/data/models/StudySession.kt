/**
 * StudySession.kt
 * -------------------------
 * Data model for study sessions.
 * Tracks user study progress, session metrics, and performance analytics.
 * Used for progress tracking, streak calculation, and study statistics.
 */

package week11.st8907.finalproject.data.models

import java.util.Date

data class StudySession(
    val sessionId: String = "",
    val userId: String = "",
    val deckId: String? = null,
    val deckName: String = "",
    val sessionType: String = "study",
    val score: Int = 0,
    val totalCards: Int = 0,
    val correctAnswers: Int = 0,
    val incorrectAnswers: Int = 0,
    val skippedCards: Int = 0,
    val date: Date = Date(),
    val duration: Int = 0,
    val xpEarned: Int = 0,
    val cardsStudied: List<String> = emptyList(),
    val difficultyBreakdown: Map<String, Int> = emptyMap()
) {

    // Calculates the session accuracy percentage
    fun calculateAccuracy(): Float {
        val totalAttempted = correctAnswers + incorrectAnswers
        return if (totalAttempted > 0) {
            (correctAnswers.toFloat() / totalAttempted) * 100
        } else {
            0f
        }
    }


    // Calculates the average time per card in seconds
    fun calculateAverageTimePerCard(): Float {
        val totalStudied = correctAnswers + incorrectAnswers + skippedCards
        return if (totalStudied > 0 && duration > 0) {
            duration.toFloat() / totalStudied
        } else {
            0f
        }
    }

    // Determines if the session was successful based on accuracy
    fun isSuccessful(minimumAccuracy: Float = 70f): Boolean {
        return calculateAccuracy() >= minimumAccuracy
    }

    companion object {
        // Creates a new study session with default values
        fun createDefault(
            userId: String,
            deckId: String? = null,
            deckName: String = "",
            sessionType: String = "study"
        ): StudySession {
            return StudySession(
                userId = userId,
                deckId = deckId,
                deckName = deckName,
                sessionType = sessionType
            )
        }


        fun calculateXp(
            correctAnswers: Int,
            sessionDuration: Int,
            difficultyMultiplier: Float = 1.0f
        ): Int {

            val baseXp = correctAnswers * 10


            val timeBonus = if (sessionDuration < 300) 50 else 75 - (sessionDuration / 10)

            // Apply difficulty multiplier
            return ((baseXp + timeBonus.coerceIn(0, 50)) * difficultyMultiplier).toInt()
        }
    }
}

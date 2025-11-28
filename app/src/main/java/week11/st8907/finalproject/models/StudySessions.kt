package week11.st8907.finalproject.models


import java.util.Date

data class StudySession(
    val sessionId: String = "",
    val userId: String = "",
    val deckId: String? = null,
    val deckName: String = "",
    val sessionType: String = "study",   // study or quiz
    val score: Int = 0,
    val totalCards: Int = 0,
    val correctAnswers: Int = 0,
    val incorrectAnswers: Int = 0,
    val skippedCards: Int = 0,
    val date: Date = Date(),
    val duration: Int = 0,               // seconds
    val xpEarned: Int = 0,
    val cardsStudied: List<String> = emptyList(),
    val difficultyBreakdown: Map<String, Int> = emptyMap()
) {

    fun calculateAccuracy(): Float {
        val attempts = correctAnswers + incorrectAnswers
        return if (attempts > 0) (correctAnswers.toFloat() / attempts) * 100f else 0f
    }

    fun calculateAverageTimePerCard(): Float {
        val total = correctAnswers + incorrectAnswers + skippedCards
        return if (total > 0 && duration > 0) duration.toFloat() / total else 0f
    }

    fun isSuccessful(minimumAccuracy: Float = 70f): Boolean {
        return calculateAccuracy() >= minimumAccuracy
    }

    companion object {

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
            duration: Int,
            difficultyMultiplier: Float = 1.0f
        ): Int {
            val baseXp = correctAnswers * 10
            val timeBonus = if (duration < 300) 50 else (75 - (duration / 10)).coerceIn(0, 50)
            return ((baseXp + timeBonus) * difficultyMultiplier).toInt()
        }
    }
}

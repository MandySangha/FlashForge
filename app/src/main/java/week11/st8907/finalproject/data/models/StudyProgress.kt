/**
 * StudyProgress.kt
 * -------------------------
 * Data model for overall study progress tracking.
 * Aggregates data from multiple study sessions for user progress overview.
 */

package week11.st8907.finalproject.data.models

import java.util.Date

data class StudyProgress(
    val userId: String = "",
    val totalSessions: Int = 0,
    val totalStudyTime: Int = 0, // in seconds
    val totalCardsStudied: Int = 0,
    val totalCorrectAnswers: Int = 0,
    val totalIncorrectAnswers: Int = 0,
    val averageAccuracy: Float = 0f,
    val currentStreak: Int = 0,
    val longestStreak: Int = 0,
    val lastStudyDate: Date? = null,
    val favoriteCategory: String = "",
    val categoryBreakdown: Map<String, Int> = emptyMap() // category -> study count
) {

    // Updates progress with new session data
    fun updateWithSession(session: StudySession): StudyProgress {
        val newTotalSessions = totalSessions + 1
        val newTotalStudyTime = totalStudyTime + session.duration
        val newTotalCardsStudied = totalCardsStudied + session.totalCards
        val newTotalCorrect = totalCorrectAnswers + session.correctAnswers
        val newTotalIncorrect = totalIncorrectAnswers + session.incorrectAnswers

        val newAverageAccuracy = if (newTotalCorrect + newTotalIncorrect > 0) {
            (newTotalCorrect.toFloat() / (newTotalCorrect + newTotalIncorrect)) * 100
        } else {
            averageAccuracy
        }

        // Update category breakdown
        val updatedCategoryBreakdown = categoryBreakdown.toMutableMap()
        val category = session.deckName.ifEmpty { "General" }
        updatedCategoryBreakdown[category] = (updatedCategoryBreakdown[category] ?: 0) + 1

        // Find favorite category
        val newFavoriteCategory = updatedCategoryBreakdown.maxByOrNull { it.value }?.key ?: favoriteCategory

        return this.copy(
            totalSessions = newTotalSessions,
            totalStudyTime = newTotalStudyTime,
            totalCardsStudied = newTotalCardsStudied,
            totalCorrectAnswers = newTotalCorrect,
            totalIncorrectAnswers = newTotalIncorrect,
            averageAccuracy = newAverageAccuracy,
            categoryBreakdown = updatedCategoryBreakdown,
            favoriteCategory = newFavoriteCategory,
            lastStudyDate = session.date
        )
    }

    // Calculates total study time in minuteS
    fun getTotalStudyTimeInMinutes(): Int {
        return totalStudyTime / 60
    }

    // Calculates overall accuracy percentage
    fun getOverallAccuracy(): Float {
        val totalAttempts = totalCorrectAnswers + totalIncorrectAnswers
        return if (totalAttempts > 0) {
            (totalCorrectAnswers.toFloat() / totalAttempts) * 100
        } else {
            0f
        }
    }
}
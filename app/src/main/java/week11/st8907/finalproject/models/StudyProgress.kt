package week11.st8907.finalproject.models


import java.util.Date

data class StudyProgress(
    val userId: String = "",
    val totalSessions: Int = 0,
    val totalStudyTime: Int = 0,
    val totalCardsStudied: Int = 0,
    val totalCorrectAnswers: Int = 0,
    val totalIncorrectAnswers: Int = 0,
    val averageAccuracy: Float = 0f,
    val currentStreak: Int = 0,
    val longestStreak: Int = 0,
    val lastStudyDate: Date? = null,
    val favoriteCategory: String = "",
    val categoryBreakdown: Map<String, Int> = emptyMap()
) {

    fun updateWithSession(session: StudySession): StudyProgress {
        val newTotalSessions = totalSessions + 1
        val newStudyTime = totalStudyTime + session.duration
        val newCards = totalCardsStudied + session.totalCards
        val newCorrect = totalCorrectAnswers + session.correctAnswers
        val newIncorrect = totalIncorrectAnswers + session.incorrectAnswers

        val accuracy = if (newCorrect + newIncorrect > 0) {
            (newCorrect.toFloat() / (newCorrect + newIncorrect)) * 100
        } else averageAccuracy

        val updatedCategories = categoryBreakdown.toMutableMap()
        val category = session.deckName.ifEmpty { "General" }
        updatedCategories[category] = (updatedCategories[category] ?: 0) + 1

        val favCategory = updatedCategories.maxByOrNull { it.value }?.key ?: favoriteCategory

        return copy(
            totalSessions = newTotalSessions,
            totalStudyTime = newStudyTime,
            totalCardsStudied = newCards,
            totalCorrectAnswers = newCorrect,
            totalIncorrectAnswers = newIncorrect,
            averageAccuracy = accuracy,
            categoryBreakdown = updatedCategories,
            favoriteCategory = favCategory,
            lastStudyDate = session.date
        )
    }

    fun getTotalStudyTimeInMinutes(): Int = totalStudyTime / 60

    fun getOverallAccuracy(): Float {
        val total = totalCorrectAnswers + totalIncorrectAnswers
        return if (total > 0) (totalCorrectAnswers.toFloat() / total) * 100 else 0f
    }
}

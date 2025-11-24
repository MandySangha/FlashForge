/**
 * DeckStatistics.kt
 * -------------------------
 * Data class for deck statistics with proper typing.
 */

package week11.st8907.finalproject.data.models

import java.util.Date

data class DeckStatistics(
    val totalCards: Int,
    val easyCards: Int,
    val mediumCards: Int,
    val hardCards: Int,
    val lastReviewed: Date?,
    val averageDifficulty: Double
)
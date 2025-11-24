/**
 * User.kt
 * -------------------------
 * Data model for user profiles.
 * Contains user information, progress tracking, and study statistics.
 */

package week11.st8907.finalproject.data.models

import java.util.Date

data class User(
    val userId: String = "",
    val email: String = "",
    val name: String = "",
    val xp: Int = 0,
    val streak: Int = 0,
    val lastStudyDate: Date? = null,
    val createdAt: Date = Date(),
    val totalCardsCreated: Int = 0,
    val totalStudySessions: Int = 0
)
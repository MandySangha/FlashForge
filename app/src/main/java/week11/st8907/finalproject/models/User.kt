package week11.st8907.finalproject.models

import com.google.firebase.Timestamp


data class User(
    val userId: String = "",
    val email: String = "",
    val name: String = "",
    val xp: Int = 0,
    val streak: Int = 0,
    val lastStudyDate: Long? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val totalCardsCreated: Int = 0,
    val totalStudySessions: Int = 0
)

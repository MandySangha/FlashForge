package week11.st8907.finalproject.data.models

data class User(
    val userId: String = "",
    val email: String = "",
    val name: String = "",
    val xp: Int = 0,
    val streak: Int = 0,

    // Use Long for Firestore compatibility (prevents Date conversion crash)
    val lastStudyDate: Long? = null,
    val createdAt: Long = System.currentTimeMillis(),

    val totalCardsCreated: Int = 0,
    val totalStudySessions: Int = 0
)

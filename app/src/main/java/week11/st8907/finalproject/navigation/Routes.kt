package week11.st8907.finalproject.navigation

object Routes {

    // Auth
    const val Splash = "splash"
    const val Login = "login"
    const val Register = "register"
    const val ForgotPassword = "forgot_password"

    // Home
    const val Home = "home"

    // Flashcards
    const val CreateFlashCard = "create_flashcard"
    const val ScanFlashCard = "scan_flashcard"

    // Firestore-required cardId routes
    const val FlashCardList = "flashcard_list"
    const val FlashCardDetail = "flashcard_detail"
    const val EditFlashCard = "edit_flashcard"
    const val StudyMode = "study_mode"

    // Quiz
    const val QuizMode = "quiz_mode"
    const val QuizResult = "quiz_result"

    // Extra pages
    const val Stats = "stats"
    const val Settings = "settings"
    const val Profile = "profile"
    const val SearchFlashCard = "search_flashcard"

    // ------------------------
    // Route Builders With Args
    // ------------------------

    fun flashcardDetail(cardId: String) = "$FlashCardDetail/$cardId"
    fun editFlashcard(cardId: String) = "$EditFlashCard/$cardId"
    fun studyMode(cardId: String) = "$StudyMode/$cardId"
}

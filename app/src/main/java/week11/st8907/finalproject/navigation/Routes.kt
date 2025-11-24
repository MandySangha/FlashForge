package week11.st8907.finalproject.navigation

/**
 * Routes.kt
 * -------------------------------
 * This file defines all navigation routes used across the FlashForge app.
 * Keeping routes in one place helps maintain clean structure, prevents typos,
 * and makes the navigation graph easy to expand as the project grows.
 */

object Routes {
    const val Splash = "splash_screen"
    const val Login = "login_screen"
    const val Register = "register_screen"
    const val ForgotPassword = "forgot_password_screen"

    const val Home = "home_screen"
    const val CreateFlashCard = "create_flashcard_screen"
    const val ScanFlashCard = "scan_flashcard_screen"

    const val FlashCardList = "flashcard_list_screen"
    const val FlashCardDetail = "flashcard_detail_screen"
    const val EditFlashCard = "edit_flashcard_screen"

    const val StudyMode = "study_mode_screen"
    const val QuizMode = "quiz_mode_screen"
    const val QuizResult = "quiz_result_screen"

    const val Stats = "stats_screen"
    const val Settings = "settings_screen"
    const val Profile = "profile_screen"
    const val SearchFlashCard = "search_flashcard_screen"
}

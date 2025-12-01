package week11.st8907.finalproject.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.NavType
import androidx.navigation.navArgument
import week11.st8907.finalproject.screens.*
import week11.st8907.finalproject.viewmodels.FlashcardViewModel

@Composable
fun AppNavGraph(navController: NavHostController) {

    // ⭐ ONE shared ViewModel for ALL flashcard screens
    val flashcardViewModel: FlashcardViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = Routes.Splash
    ) {

        // Auth
        composable(Routes.Splash) { SplashScreen(navController) }
        composable(Routes.Login) { LoginScreen(navController) }
        composable(Routes.Register) { RegisterScreen(navController) }
        composable(Routes.ForgotPassword) { ForgotPasswordScreen(navController) }

        // Home
        composable(Routes.Home) { HomeScreen(navController) }

        // Create & Scan
        composable(Routes.CreateFlashCard) {
            CreateFlashCardScreen(navController, flashcardViewModel)
        }

        composable(Routes.ScanFlashCard) {
            ScanFlashCardScreen(navController)
        }

        // List
        composable(Routes.FlashCardList) {
            FlashCardListScreen(navController, flashcardViewModel)
        }

        // DETAIL
        composable(
            route = "${Routes.FlashCardDetail}/{cardId}",
            arguments = listOf(navArgument("cardId") { type = NavType.StringType })
        ) { entry ->
            val cardId = entry.arguments?.getString("cardId") ?: ""
            FlashCardDetailScreen(navController, cardId, flashcardViewModel)
        }

        // EDIT
        composable(
            route = "${Routes.EditFlashCard}/{cardId}",
            arguments = listOf(navArgument("cardId") { type = NavType.StringType })
        ) { entry ->
            val cardId = entry.arguments?.getString("cardId") ?: ""
            EditFlashCardScreen(navController, cardId, flashcardViewModel)
        }

        // STUDY MODE
        composable(Routes.StudyMode) {
            StudyModeScreen(navController, flashcardViewModel)
        }


        // Quiz
        composable(
            route = "${Routes.QuizResult}/{score}/{total}"
        ) { entry ->
            val score = entry.arguments?.getString("score")?.toIntOrNull() ?: 0
            val total = entry.arguments?.getString("total")?.toIntOrNull() ?: 0
            QuizResultScreen(navController, score, total)
        }

        composable(Routes.QuizMode) {
            QuizModeScreen(navController, flashcardViewModel)
        }

        composable("${Routes.QuizResult}/{score}/{total}") { entry ->
            val score = entry.arguments?.getString("score")?.toIntOrNull() ?: 0
            val total = entry.arguments?.getString("total")?.toIntOrNull() ?: 0
            QuizResultScreen(navController, score, total)
        }



        // Other
        composable(Routes.Stats) { StatsScreen(navController) }
        composable(Routes.Settings) { SettingsScreen(navController) }
        composable(Routes.Profile) { ProfileScreen(navController) }

        // Search
        composable(Routes.SearchFlashCard) {
            SearchFlashCardScreen(navController, flashcardViewModel)
        }
    }
}

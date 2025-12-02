package week11.st8907.finalproject.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.NavType
import androidx.navigation.navArgument
import week11.st8907.finalproject.screens.*
import week11.st8907.finalproject.data.viewmodels.FlashcardViewModel
import week11.st8907.finalproject.viewmodel.OCRViewModel

@Composable
fun AppNavGraph(navController: NavHostController) {

    // Shared ViewModels
    val flashcardViewModel: FlashcardViewModel = viewModel()
    val ocrViewModel: OCRViewModel = viewModel()

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
            CreateFlashCardScreen(
                navController = navController,
                flashcardViewModel = flashcardViewModel,
                ocrViewModel = ocrViewModel
            )
        }

        composable(Routes.ScanFlashCard) {
            ScanFlashCardScreen(
                navController = navController,
                ocrViewModel = ocrViewModel
            )
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

        // EDIT PROFILE
        composable(Routes.EditProfile) {
            EditProfileScreen(navController)
        }

        // STUDY MODE
        composable(Routes.StudyMode) {
            StudyModeScreen(navController, flashcardViewModel)
        }

        // QUIZ
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

        // Search
        composable(Routes.SearchFlashCard) {
            SearchFlashCardScreen(navController, flashcardViewModel)
        }
    }
}

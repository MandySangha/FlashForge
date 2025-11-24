package week11.st8907.finalproject.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import week11.st8907.finalproject.screens.*

/**
 * NavGraph.kt
 * -------------------------------------
 * This file contains the full navigation graph for FlashForge.
 * Every screen connects here, enabling smooth movement between app sections.
 * For Step 4, screens only show headings and dummy navigation buttons.
 */

@Composable
fun AppNavGraph(navController: NavHostController) {

    NavHost(
        navController = navController,
        startDestination = Routes.Splash
    ) {

        composable(Routes.Splash) { SplashScreen(navController) }
        composable(Routes.Login) { LoginScreen(navController) }
        composable(Routes.Register) { RegisterScreen(navController) }
        composable(Routes.ForgotPassword) { ForgotPasswordScreen(navController) }

        composable(Routes.Home) { HomeScreen(navController) }
        composable(Routes.CreateFlashCard) { CreateFlashCardScreen(navController) }
        composable(Routes.ScanFlashCard) { ScanFlashCardScreen(navController) }

        composable(Routes.FlashCardList) { FlashCardListScreen(navController) }
        composable(Routes.FlashCardDetail) { FlashCardDetailScreen(navController) }
        composable(Routes.EditFlashCard) { EditFlashCardScreen(navController) }

        composable(Routes.StudyMode) { StudyModeScreen(navController) }
        composable(Routes.QuizMode) { QuizModeScreen(navController) }
        composable(Routes.QuizResult) { QuizResultScreen(navController) }

        composable(Routes.Stats) { StatsScreen(navController) }
        composable(Routes.Settings) { SettingsScreen(navController) }
        composable(Routes.Profile) { ProfileScreen(navController) }
        composable(Routes.SearchFlashCard) { SearchFlashCardScreen(navController) }
    }
}

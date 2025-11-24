package week11.st8907.finalproject.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import week11.st8907.finalproject.navigation.Routes

/**
 * SplashScreen.kt
 * -------------------------------------------------------------
 * This is the initial launch screen of the FlashForge app.
 * For Step 4, this screen only shows a simple heading and a
 * temporary button that navigates to the Login screen. Later,
 * this screen will handle auto-navigation based on user auth state.
 */

@Composable
fun SplashScreen(navController: NavController) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        Column(horizontalAlignment = Alignment.CenterHorizontally) {

            Text(
                text = "Splash Screen",
                fontSize = 28.sp,
                modifier = Modifier.padding(bottom = 40.dp)
            )

            Button(
                onClick = { navController.navigate(Routes.Login) }
            ) {
                Text("Go to Login")
            }
        }
    }
}

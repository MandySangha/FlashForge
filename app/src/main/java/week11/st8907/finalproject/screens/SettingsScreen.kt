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
 * SettingsScreen.kt
 * -------------------------------------------------------------
 * This screen will hold user settings such as account info, theme
 * preferences, and logout functionality. For Step 4, we display a
 * heading and simple navigation options only.
 */

@Composable
fun SettingsScreen(navController: NavController) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = "Settings Screen",
            fontSize = 28.sp,
            modifier = Modifier.padding(bottom = 40.dp)
        )

        Button(
            onClick = { navController.navigate(Routes.Profile) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Go to Profile")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { navController.navigate(Routes.Home) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Back to Home")
        }
    }
}

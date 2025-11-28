package week11.st8907.finalproject.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import week11.st8907.finalproject.navigation.Routes
import week11.st8907.finalproject.ui.theme.*

@Composable
fun SettingsScreen(navController: NavController) {

    var darkMode by remember { mutableStateOf(false) }
    var notifications by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(22.dp),
        horizontalAlignment = Alignment.Start
    ) {

        Text(
            text = "Settings",
            fontSize = 28.sp,
            color = NeonText,
            modifier = Modifier.padding(bottom = 20.dp)
        )

        SettingItem(
            title = "Dark Mode",
            checked = darkMode,
            onToggle = { darkMode = it }
        )

        Spacer(Modifier.height(16.dp))

        SettingItem(
            title = "Notifications",
            checked = notifications,
            onToggle = { notifications = it }
        )

        Spacer(Modifier.height(35.dp))

        NeonButton(
            text = "Logout",
            onClick = {
                navController.navigate(Routes.Login) {
                    popUpTo(Routes.Home) { inclusive = true }
                }
            }
        )
    }
}

@Composable
fun SettingItem(title: String, checked: Boolean, onToggle: (Boolean) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.horizontalGradient(
                    listOf(NeonPurple.copy(alpha = 0.4f), NeonPink.copy(alpha = 0.4f))
                ),
                RoundedCornerShape(14.dp)
            )
            .padding(18.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(title, color = NeonText, fontSize = 18.sp)
        Switch(checked = checked, onCheckedChange = onToggle)
    }
}

package week11.st8907.finalproject.screens

import com.google.firebase.auth.FirebaseAuth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import week11.st8907.finalproject.navigation.Routes
import week11.st8907.finalproject.ui.theme.*

@Composable
fun ProfileScreen(navController: NavController) {

    // Placeholder profile data
    val name = "FlashForge User"
    val email = "user@example.com"
    val xp = 2400
    val streak = 5

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(22.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Profile",
            fontSize = 28.sp,
            color = NeonText,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 20.dp)
        )

        // Avatar Circle
        Box(
            modifier = Modifier
                .size(110.dp)
                .clip(CircleShape)
                .background(
                    Brush.linearGradient(
                        listOf(NeonPurple, NeonPink)
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = name.first().toString(),
                color = Color.White,
                fontSize = 45.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(Modifier.height(20.dp))

        // Name + Email
        Text(name, color = NeonText, fontSize = 22.sp, fontWeight = FontWeight.Medium)
        Text(email, color = Color.LightGray, fontSize = 14.sp)

        Spacer(Modifier.height(25.dp))

        // Stats Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            ProfileStat(title = "XP", value = xp.toString())
            ProfileStat(title = "Streak", value = "$streak Days")
        }

        Spacer(Modifier.height(40.dp))

        NeonButton(
            text = "Edit Profile",
            onClick = { /* placeholder */ }
        )

        NeonButton(
            text = "Logout",
            onClick = {
                FirebaseAuth.getInstance().signOut()
                navController.navigate(Routes.Login) {
                    popUpTo(Routes.Home) { inclusive = true }
                }
            }
        )

    }
}

@Composable
fun ProfileStat(title: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            fontSize = 24.sp,
            color = NeonText,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = title,
            fontSize = 14.sp,
            color = Color.Gray
        )
    }
}

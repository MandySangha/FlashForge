package week11.st8907.finalproject.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import week11.st8907.finalproject.navigation.Routes
import week11.st8907.finalproject.ui.theme.NeonPink
import week11.st8907.finalproject.ui.theme.NeonPurple

@Composable
fun SplashScreen(navController: NavController) {

    // animations
    val scaleAnimation = rememberInfiniteTransition()
    val scale by scaleAnimation.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.10f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    val alphaAnim by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(durationMillis = 1500)
    )

    LaunchedEffect(true) {
        delay(1800)
        navController.navigate(Routes.Login) {
            popUpTo(Routes.Splash) { inclusive = true }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

       /* Image(
            painter = painterResource(id = R.drawable.flashforge_logo),
            contentDescription = "Logo",
            modifier = Modifier
                .size(160.dp)
                .scale(scale)
                .alpha(alphaAnim),
            colorFilter = ColorFilter.tint(NeonPink)
        ) */

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "FlashForge",
            fontSize = 36.sp,
            color = NeonPurple,
            modifier = Modifier.alpha(alphaAnim)
        )
    }
}

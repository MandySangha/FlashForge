package week11.st8907.finalproject.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import week11.st8907.finalproject.components.PrimaryButton
import week11.st8907.finalproject.data.viewmodels.ProfileViewModel
import week11.st8907.finalproject.navigation.Routes
import week11.st8907.finalproject.ui.theme.NeonPink
import week11.st8907.finalproject.ui.theme.NeonPurple
import week11.st8907.finalproject.ui.theme.NeonText
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.draw.shadow
import com.google.firebase.auth.FirebaseAuth
import week11.st8907.finalproject.components.SecondaryButton
import week11.st8907.finalproject.data.viewmodels.FlashcardViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavController) {
    val profileViewModel: ProfileViewModel = viewModel()
    val flashcardViewModel: FlashcardViewModel = viewModel()

    val currentUser by profileViewModel.currentUser.collectAsState()
    val userFlashcards by flashcardViewModel.userFlashcards.collectAsState()

    // Load user data and flashcards when screen appears
    LaunchedEffect(Unit) {
        profileViewModel.loadUserProfile()

        // Load flashcards for current user
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (!userId.isNullOrBlank()) {
            flashcardViewModel.loadUserFlashcards(userId)
        }
    }

    val name = currentUser?.name ?: "FlashForge User"
    val email = currentUser?.email ?: "user@example.com"
    val xp = currentUser?.xp ?: 0
    val streak = currentUser?.streak ?: 0
    val totalCards = userFlashcards.size
    val totalStudySessions = currentUser?.totalStudySessions ?: 0

    // Calculate level based on XP (100 XP per level)
    val level = (xp / 100) + 1
    val currentLevelXp = xp % 100

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Profile",
                        color = NeonText,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = NeonText
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.Black
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier.padding(22.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Avatar with Level Badge
                Box(
                    modifier = Modifier.size(120.dp)
                ) {
                    // Avatar
                    Box(
                        modifier = Modifier
                            .size(110.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.linearGradient(listOf(NeonPurple, NeonPink))
                            )
                            .align(Alignment.Center),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = name.firstOrNull()?.uppercaseChar()?.toString() ?: "U",
                            color = Color.White,
                            fontSize = 48.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Level Badge
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(NeonPink)
                            .border(2.dp, Color.Black, CircleShape)
                            .align(Alignment.BottomEnd),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "L$level",
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(Modifier.height(20.dp))

                // Name + Email
                Text(name, color = NeonText, fontSize = 24.sp, fontWeight = FontWeight.Medium)
                Text(email, color = Color.LightGray, fontSize = 14.sp)

                Spacer(Modifier.height(30.dp))

                // XP Progress
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "Level $level • $xp XP",
                        color = NeonText,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(Modifier.height(8.dp))

                    // Progress Bar
                    LinearProgressIndicator(
                        progress = { currentLevelXp / 100f },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(12.dp)
                            .clip(RoundedCornerShape(6.dp)),
                        color = NeonPink,
                        trackColor = Color.DarkGray
                    )

                    Spacer(Modifier.height(4.dp))

                    Text(
                        "$currentLevelXp/100 XP to Level ${level + 1}",
                        color = Color.LightGray,
                        fontSize = 12.sp
                    )
                }
            }

            // Stats Grid
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 22.dp)
            ) {
                // Row 1
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    ProfileStatCard(
                        title = "Current Streak",
                        value = "$streak days",
                        color = NeonPink,
                        modifier = Modifier.weight(1f)
                    )
                    ProfileStatCard(
                        title = "Total XP",
                        value = "$xp",
                        color = NeonPurple,
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(Modifier.height(16.dp))

                // Row 2
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    ProfileStatCard(
                        title = "Total Cards",
                        value = totalCards.toString(),
                        color = Color(0xFF45B7D1),
                        modifier = Modifier.weight(1f)
                    )
                    ProfileStatCard(
                        title = "Study Sessions",
                        value = totalStudySessions.toString(),
                        color = Color(0xFF96CEB4),
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    ProfileStatCard(
                        title = "Level",
                        value = level.toString(),
                        color = Color(0xFF4ECDC4),
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(22.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(Modifier.height(20.dp))

                // EDIT PROFILE
                PrimaryButton(
                    text = "Edit Profile",
                    onClick = {
                        navController.navigate(Routes.EditProfile)
                    }
                )

                Spacer(Modifier.height(16.dp))

                // LOGOUT
                SecondaryButton(
                    text = "Logout",
                    onClick = {
                        FirebaseAuth.getInstance().signOut()
                        navController.navigate(Routes.Login) {
                            popUpTo(Routes.Home) { inclusive = true }
                        }
                    },
                )
            }
        }
    }
}

@Composable
fun ProfileStatCard(
    title: String,
    value: String,
    color: Color = NeonText,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .height(100.dp)
            .shadow(6.dp, RoundedCornerShape(10.dp))
            .background(Color(0xFF1B1B1D), RoundedCornerShape(10.dp))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = value,
            fontSize = 24.sp,
            color = color,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = title,
            fontSize = 14.sp,
            color = Color.Gray
        )
    }
}
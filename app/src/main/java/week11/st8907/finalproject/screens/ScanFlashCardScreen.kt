package week11.st8907.finalproject.screens

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import week11.st8907.finalproject.components.CameraPreview
import week11.st8907.finalproject.navigation.Routes
import week11.st8907.finalproject.ui.theme.NeonPink
import week11.st8907.finalproject.ui.theme.NeonPurple
import week11.st8907.finalproject.ui.theme.NeonText
import week11.st8907.finalproject.viewmodel.OCRViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScanFlashCardScreen(
    navController: NavController,
    ocrViewModel: OCRViewModel
) {

    val context = LocalContext.current
    val viewModel = ocrViewModel

    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context, Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        hasCameraPermission = granted
    }


    val storagePermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {}


    val readImagesLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {}

    // GALLERY PICKER
    val pickImageLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            viewModel.processImageFromUri(it, context)
            navController.navigate(Routes.CreateFlashCard)
        }
    }

    LaunchedEffect(Unit) {
        if (!hasCameraPermission) {
            permissionLauncher.launch(Manifest.permission.CAMERA)
        }

        if (android.os.Build.VERSION.SDK_INT >= 33) {
            readImagesLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
        } else {
            storagePermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Scan Flashcard", color = NeonText) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null, tint = NeonText)
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
                .padding(22.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            if (!hasCameraPermission) {
                Text("Camera permission is required.", color = Color.White)
                Button(onClick = {
                    permissionLauncher.launch(Manifest.permission.CAMERA)
                }) { Text("Grant Permission") }
                return@Column
            }

            // CAMERA PREVIEW
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(330.dp)
                    .shadow(10.dp, RoundedCornerShape(18.dp))
                    .border(BorderStroke(3.dp, NeonPink), RoundedCornerShape(18.dp))
            ) {
                CameraPreview(
                    modifier = Modifier.fillMaxSize(),
                    onPhotoCaptured = { bitmap ->
                        viewModel.processImageFromBitmap(bitmap)
                        navController.navigate(Routes.CreateFlashCard)
                    }
                )
            }

            Spacer(Modifier.height(36.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(58.dp)
                    .shadow(8.dp, RoundedCornerShape(14.dp))
                    .background(
                        Brush.horizontalGradient(listOf(NeonPurple, NeonPink)),
                        RoundedCornerShape(14.dp)
                    )
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("Tap Preview to Scan", color = Color.White, fontSize = 18.sp)
            }

            Spacer(Modifier.height(20.dp))

            // GALLERY PICK
            Button(
                onClick = { pickImageLauncher.launch("image/*") },
                colors = ButtonDefaults.buttonColors(containerColor = NeonPurple)
            ) {
                Text("Pick From Gallery", color = Color.White)
            }

            Spacer(Modifier.height(12.dp))

            TextButton(onClick = {
                navController.navigate(Routes.CreateFlashCard)
            }) {
                Text("Or Enter Manually", color = NeonPink)
            }
        }
    }
}

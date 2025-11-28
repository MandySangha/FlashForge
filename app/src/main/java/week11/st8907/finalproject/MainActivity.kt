package week11.st8907.finalproject

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Surface
import androidx.navigation.compose.rememberNavController
import week11.st8907.finalproject.navigation.AppNavGraph
import week11.st8907.finalproject.ui.theme.FlashForgeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            FlashForgeTheme {
                val navController = rememberNavController()
                Surface {
                    AppNavGraph(navController)
                }
            }
        }
    }
}

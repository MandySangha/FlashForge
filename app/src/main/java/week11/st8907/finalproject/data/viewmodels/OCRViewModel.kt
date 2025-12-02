package week11.st8907.finalproject.viewmodel

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class OCRViewModel : ViewModel() {

    private val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

    private val _recognizedText = MutableStateFlow("")
    val recognizedText = _recognizedText.asStateFlow()

    fun processImageFromBitmap(bitmap: Bitmap) {
        Log.d("OCR_DEBUG", "Bitmap received for OCR")
        val image = InputImage.fromBitmap(bitmap, 0)
        runOCR(image)
    }

    fun processImageFromUri(uri: Uri, context: Context) {
        Log.d("OCR_DEBUG", "URI received = $uri")

        try {
            val image = InputImage.fromFilePath(context, uri)
            Log.d("OCR_DEBUG", "InputImage created successfully from URI")

            runOCR(image)

        } catch (e: Exception) {
            Log.e("OCR_DEBUG", "FAILED to load InputImage from URI", e)
        }
    }

    fun clearRecognizedText() {
        _recognizedText.value = ""
    }


    private fun runOCR(image: InputImage) {
        Log.d("OCR_DEBUG", "Starting ML Kit text recognition...")

        recognizer.process(image)
            .addOnSuccessListener { result ->
                Log.d("OCR_DEBUG", "OCR SUCCESS: ${result.text}")
                _recognizedText.value = result.text
            }
            .addOnFailureListener { e ->
                Log.e("OCR_DEBUG", "OCR FAILED", e)
            }
    }
}

package week11.st8907.finalproject.data.models

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions

class OCRProcessor {

    private val recognizer =
        TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

    /**
     * Process an image from Bitmap
     */
    fun processBitmap(
        bitmap: Bitmap,
        onResult: (String) -> Unit,
        onError: (Exception) -> Unit
    ) {
        try {
            val image = InputImage.fromBitmap(bitmap, 0)
            recognize(image, onResult, onError)
        } catch (e: Exception) {
            onError(e)
        }
    }

    /**
     * Process an image from Uri (Gallery)
     */
    fun processUri(
        uri: Uri,
        context: Context,
        onResult: (String) -> Unit,
        onError: (Exception) -> Unit
    ) {
        try {
            val image = InputImage.fromFilePath(context, uri)
            recognize(image, onResult, onError)
        } catch (e: Exception) {
            onError(e)
        }
    }

    /**
     * Core ML Kit text recognition
     */
    private fun recognize(
        image: InputImage,
        onResult: (String) -> Unit,
        onError: (Exception) -> Unit
    ) {
        recognizer.process(image)
            .addOnSuccessListener { result ->
                onResult(result.text)
            }
            .addOnFailureListener { e ->
                onError(e)
            }
    }
}

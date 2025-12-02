package week11.st8907.finalproject.data.viewmodels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ScanViewModel : ViewModel() {

    private val _scannedText = MutableStateFlow("")
    val scannedText: StateFlow<String> = _scannedText

    fun updateScannedText(text: String) {
        _scannedText.value = text
    }

    fun clearScannedText() {
        _scannedText.value = ""
    }
}

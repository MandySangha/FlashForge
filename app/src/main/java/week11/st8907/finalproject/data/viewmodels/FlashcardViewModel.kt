/**
 * FlashcardViewModel.kt
 * -------------------------
 * ViewModel for flashcard management.
 * Simple implementation without Hilt dependency injection.
 */
package week11.st8907.finalproject.data.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import week11.st8907.finalproject.data.models.Flashcard
import week11.st8907.finalproject.data.repositories.FlashcardRepository

class FlashcardViewModel : ViewModel() {
    private val firestore = FirebaseFirestore.getInstance()
    private val flashcardRepository = FlashcardRepository()

    private val _userFlashcards = MutableStateFlow<List<Flashcard>>(emptyList())
    val userFlashcards: StateFlow<List<Flashcard>> = _userFlashcards.asStateFlow()

    private val _currentFlashcard = MutableStateFlow<Flashcard?>(null)
    val currentFlashcard: StateFlow<Flashcard?> = _currentFlashcard.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    // CREATE - Create new flashcard
    fun createFlashcard(flashcard: Flashcard, onSuccess: (String) -> Unit = {}) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            val result = flashcardRepository.createFlashcard(flashcard)
            if (result.isSuccess) {
                onSuccess(result.getOrThrow())
                // Reload flashcards if we have a userId
                if (flashcard.userId.isNotBlank()) {
                    loadUserFlashcards(flashcard.userId)
                }
            } else {
                _errorMessage.value = "Failed to create flashcard: ${result.exceptionOrNull()?.message}"
            }
            _isLoading.value = false
        }
    }

    // READ - Load all user flashcards
    fun loadUserFlashcards(userId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            flashcardRepository.getUserFlashcards(userId).collect { flashcards ->
                _userFlashcards.value = flashcards
                _isLoading.value = false
            }
        }
    }

    // READ - Load single flashcard
    fun loadFlashcard(cardId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            val result = flashcardRepository.getFlashcard(cardId)
            if (result.isSuccess) {
                _currentFlashcard.value = result.getOrThrow()
            } else {
                _errorMessage.value = "Failed to load flashcard: ${result.exceptionOrNull()?.message}"
            }
            _isLoading.value = false
        }
    }

    // UPDATE - Update flashcard
    fun updateFlashcard(flashcard: Flashcard, onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            val result = flashcardRepository.updateFlashcard(flashcard)
            if (result.isSuccess) {
                onSuccess()
                // Reload flashcards if we have a userId
                if (flashcard.userId.isNotBlank()) {
                    loadUserFlashcards(flashcard.userId)
                }
            } else {
                _errorMessage.value = "Failed to update flashcard: ${result.exceptionOrNull()?.message}"
            }
            _isLoading.value = false
        }
    }

    // DELETE - Delete flashcard
    fun deleteFlashcard(cardId: String, userId: String, onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            val result = flashcardRepository.deleteFlashcard(cardId)
            if (result.isSuccess) {
                onSuccess()
                loadUserFlashcards(userId)
            } else {
                _errorMessage.value = "Failed to delete flashcard: ${result.exceptionOrNull()?.message}"
            }
            _isLoading.value = false
        }
    }

    // SEARCH - Search flashcards
    fun searchFlashcards(userId: String, query: String, onResult: (List<Flashcard>) -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = flashcardRepository.searchFlashcards(userId, query)
            if (result.isSuccess) {
                onResult(result.getOrThrow())
            } else {
                _errorMessage.value = "Search failed: ${result.exceptionOrNull()?.message}"
                onResult(emptyList())
            }
            _isLoading.value = false
        }
    }

    fun clearError() {
        _errorMessage.value = null
    }
}
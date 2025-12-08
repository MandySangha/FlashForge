package week11.st8907.finalproject.data.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import week11.st8907.finalproject.data.models.Flashcard
import week11.st8907.finalproject.data.repositories.FlashcardRepository
import week11.st8907.finalproject.data.repositories.UserRepository

class FlashcardViewModel : ViewModel() {
    private val flashcardRepository = FlashcardRepository()
    private val userRepository = UserRepository()
    private val auth = FirebaseAuth.getInstance()

    private val _userFlashcards = MutableStateFlow<List<Flashcard>>(emptyList())
    val userFlashcards: StateFlow<List<Flashcard>> = _userFlashcards.asStateFlow()

    private val _currentFlashcard = MutableStateFlow<Flashcard?>(null)
    val currentFlashcard: StateFlow<Flashcard?> = _currentFlashcard.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _questionText = MutableStateFlow("")
    val questionText: StateFlow<String> = _questionText.asStateFlow()

    private val _answerText = MutableStateFlow("")
    val answerText: StateFlow<String> = _answerText.asStateFlow()

    fun setExtractedText(extracted: String) {
        val lines = extracted.lines()
        val q = lines.firstOrNull()?.trim().orEmpty()
        val a = if (lines.size > 1) lines.drop(1).joinToString("\n").trim() else ""
        _questionText.value = q
        _answerText.value = a
    }

    fun trackStudyProgress(cardsStudied: Int, correctAnswers: Int, onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            _isLoading.value = true
            val userId = auth.currentUser?.uid
            if (userId == null) {
                _errorMessage.value = "User not authenticated"
                _isLoading.value = false
                return@launch
            }
            try {
                val baseXp = cardsStudied * 10
                val accuracy = if (cardsStudied > 0) correctAnswers.toDouble() / cardsStudied else 0.0
                val bonusXp = if (accuracy > 0.8) (baseXp * 0.2).toInt() else 0
                val totalXp = baseXp + bonusXp
                val result = userRepository.updateUserProgress(userId, totalXp)
                if (result.isSuccess) {
                    onSuccess()
                } else {
                    _errorMessage.value = "Failed to update progress: ${result.exceptionOrNull()?.message}"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error tracking study: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun createFlashcard(flashcard: Flashcard, onSuccess: (String) -> Unit = {}) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            val result = flashcardRepository.createFlashcard(flashcard)
            if (result.isSuccess) {
                onSuccess(result.getOrThrow())
                if (flashcard.userId.isNotBlank()) {
                    userRepository.updateTotalCardsCreated(flashcard.userId)
                    loadUserFlashcards(flashcard.userId)
                }
            } else {
                _errorMessage.value = "Failed to create flashcard: ${result.exceptionOrNull()?.message}"
            }
            _isLoading.value = false
        }
    }

    fun awardDailyStudyXP(userId: String, onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            try {
                val result = userRepository.updateUserProgress(userId, 20)
                if (result.isSuccess) {
                    onSuccess()
                } else {
                    val error = result.exceptionOrNull()?.message ?: "Unknown error"
                    _errorMessage.value = "Failed to update progress: $error"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error: ${e.message}"
            }
        }
    }

    fun loadUserFlashcards(userId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                flashcardRepository.getUserFlashcards(userId).collect { flashcards ->
                    _userFlashcards.value = flashcards
                }
            } catch (e: Exception) {
                _errorMessage.value = "Failed to load flashcards: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

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

    fun updateFlashcard(flashcard: Flashcard, onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            val result = flashcardRepository.updateFlashcard(flashcard)
            if (result.isSuccess) {
                onSuccess()
                if (flashcard.userId.isNotBlank()) {
                    loadUserFlashcards(flashcard.userId)
                }
            } else {
                _errorMessage.value = "Failed to update flashcard: ${result.exceptionOrNull()?.message}"
            }
            _isLoading.value = false
        }
    }

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

    fun resetLoading() {
        _isLoading.value = false
    }
}

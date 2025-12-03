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

    // Track study progress
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
                // Calculate XP (10 XP per card, bonus for accuracy)
                val baseXp = cardsStudied * 10
                val accuracy = if (cardsStudied > 0) correctAnswers.toDouble() / cardsStudied else 0.0
                val bonusXp = if (accuracy > 0.8) (baseXp * 0.2).toInt() else 0
                val totalXp = baseXp + bonusXp

                println("DEBUG FlashcardVM: Tracking study - Cards: $cardsStudied, Correct: $correctAnswers, XP: $totalXp")

                // Update user progress
                val result = userRepository.updateUserProgress(userId, totalXp)
                if (result.isSuccess) {
                    println("DEBUG FlashcardVM: Successfully updated user progress")
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

    // CREATE - Create new flashcard
    fun createFlashcard(flashcard: Flashcard, onSuccess: (String) -> Unit = {}) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            val result = flashcardRepository.createFlashcard(flashcard)
            if (result.isSuccess) {
                onSuccess(result.getOrThrow())

                // Also update user's total cards created
                if (flashcard.userId.isNotBlank()) {
                    userRepository.updateTotalCardsCreated(flashcard.userId)
                }

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

    // Daily study XP
    fun awardDailyStudyXP(userId: String, onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            try {
                // Award 20 XP for daily study
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

    // READ - Load all user flashcards
    fun loadUserFlashcards(userId: String) {
        viewModelScope.launch {
            println("DEBUG VM: Starting loadUserFlashcards for userId: $userId")
            _isLoading.value = true
            _errorMessage.value = null

            try {
                flashcardRepository.getUserFlashcards(userId).collect { flashcards ->
                    println("DEBUG VM: Received ${flashcards.size} flashcards from repository")

                    // Debug each flashcard
                    flashcards.forEachIndexed { index, flashcard ->
                        println("DEBUG VM: Flashcard $index - ID: '${flashcard.cardId}'")
                        println("DEBUG VM: Flashcard $index - Question: '${flashcard.question}'")
                        println("DEBUG VM: Flashcard $index - UserId: '${flashcard.userId}'")
                    }

                    // Update state
                    _userFlashcards.value = flashcards
                    println("DEBUG VM: Updated _userFlashcards with ${flashcards.size} items")
                }
            } catch (e: Exception) {
                println("DEBUG VM: ERROR in loadUserFlashcards: ${e.message}")
                e.printStackTrace()
                _errorMessage.value = "Failed to load flashcards: ${e.message}"
            } finally {
                _isLoading.value = false
                println("DEBUG VM: Finished loadUserFlashcards")
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

    fun resetLoading() {
        _isLoading.value = false
    }
}
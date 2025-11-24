/**
 * DeckViewModel.kt
 * -------------------------
 * ViewModel for deck management.
 * Handles deck operations and real-time deck updates.
 */
package week11.st8907.finalproject.data.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import week11.st8907.finalproject.data.models.Deck
import week11.st8907.finalproject.data.repositories.DeckRepository

class DeckViewModel : ViewModel() {
    private val deckRepository = DeckRepository()

    private val _userDecks = MutableStateFlow<List<Deck>>(emptyList())
    val userDecks: StateFlow<List<Deck>> = _userDecks.asStateFlow()

    private val _currentDeck = MutableStateFlow<Deck?>(null)
    val currentDeck: StateFlow<Deck?> = _currentDeck.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    // CREATE - Create new deck
    fun createDeck(deck: Deck, onSuccess: (String) -> Unit = {}) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            val result = deckRepository.createDeck(deck)
            if (result.isSuccess) {
                onSuccess(result.getOrThrow())
                if (deck.userId.isNotBlank()) {
                    loadUserDecks(deck.userId)
                }
            } else {
                _errorMessage.value = "Failed to create deck: ${result.exceptionOrNull()?.message}"
            }
            _isLoading.value = false
        }
    }

    // READ - Load all user decks
    fun loadUserDecks(userId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            deckRepository.getUserDecks(userId).collect { decks ->
                _userDecks.value = decks
                _isLoading.value = false
            }
        }
    }

    // READ - Load single deck
    fun loadDeck(deckId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            val result = deckRepository.getDeck(deckId)
            if (result.isSuccess) {
                _currentDeck.value = result.getOrThrow()
            } else {
                _errorMessage.value = "Failed to load deck: ${result.exceptionOrNull()?.message}"
            }
            _isLoading.value = false
        }
    }

    // UPDATE - Update deck
    fun updateDeck(deck: Deck, onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            val result = deckRepository.updateDeck(deck)
            if (result.isSuccess) {
                onSuccess()
                if (deck.userId.isNotBlank()) {
                    loadUserDecks(deck.userId)
                }
            } else {
                _errorMessage.value = "Failed to update deck: ${result.exceptionOrNull()?.message}"
            }
            _isLoading.value = false
        }
    }

    // DELETE - Delete deck
    fun deleteDeck(deckId: String, userId: String, onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            val result = deckRepository.deleteDeck(deckId)
            if (result.isSuccess) {
                onSuccess()
                loadUserDecks(userId)
            } else {
                _errorMessage.value = "Failed to delete deck: ${result.exceptionOrNull()?.message}"
            }
            _isLoading.value = false
        }
    }

    // SEARCH - Search decks
    fun searchDecks(userId: String, query: String, onResult: (List<Deck>) -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = deckRepository.searchDecks(userId, query)
            if (result.isSuccess) {
                onResult(result.getOrThrow())
            } else {
                _errorMessage.value = "Search failed: ${result.exceptionOrNull()?.message}"
                onResult(emptyList())
            }
            _isLoading.value = false
        }
    }

    // Get decks by category
    fun loadDecksByCategory(userId: String, category: String) {
        viewModelScope.launch {
            _isLoading.value = true
            deckRepository.getDecksByCategory(userId, category).collect { decks ->
                _userDecks.value = decks
                _isLoading.value = false
            }
        }
    }

    fun clearError() {
        _errorMessage.value = null
    }
}
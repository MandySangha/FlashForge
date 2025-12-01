package week11.st8907.finalproject.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import week11.st8907.finalproject.models.Flashcard
import week11.st8907.finalproject.repositories.FlashcardRepository

class FlashcardViewModel(
    private val repo: FlashcardRepository = FlashcardRepository()
) : ViewModel() {

    private val _cards = MutableStateFlow<List<Flashcard>>(emptyList())
    val cards: StateFlow<List<Flashcard>> = _cards

    init {
        repo.getFlashcardsRealtime { list ->
            _cards.value = list
        }
    }

    fun addCard(question: String, answer: String) {
        if (question.isBlank() || answer.isBlank()) return

        val newCard = Flashcard(
            question = question,
            answer = answer,
            userId = FirebaseAuth.getInstance().currentUser!!.uid
        )

        viewModelScope.launch {
            repo.addFlashcard(newCard)
        }
    }

    fun updateCard(card: Flashcard) {
        viewModelScope.launch {
            repo.updateFlashcard(card)
        }
    }

    fun deleteCard(cardId: String) {
        viewModelScope.launch {
            repo.deleteFlashcard(cardId)
        }
    }

    fun searchFlashcards(query: String): List<Flashcard> {
        return cards.value.filter { card ->
            card.question.contains(query, true) ||
                    card.answer.contains(query, true)
        }
    }
}

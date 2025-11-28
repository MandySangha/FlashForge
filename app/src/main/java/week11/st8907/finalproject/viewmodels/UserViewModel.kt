package week11.st8907.finalproject.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import week11.st8907.finalproject.models.User
import week11.st8907.finalproject.repositories.UserRepository

class UserViewModel(
    private val repo: UserRepository = UserRepository()
) : ViewModel() {

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user

    init {
        repo.getUserRealtime { fetched ->
            _user.value = fetched
        }
    }

    fun refreshUser() {
        viewModelScope.launch {
            _user.value = repo.getUser()
        }
    }
}

package week11.st8907.finalproject.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * AuthViewModel.kt
 * -------------------------------------------------------------
 * ViewModel responsible for managing authentication state.
 * Uses StateFlow to notify UI about loading, success, or errors.
 * Firebase logic will be added in Step 4a.
 */

class AuthViewModel(
    private val repository: AuthRepository = AuthRepository()
) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState

    fun login(email: String, password: String) {
        _authState.value = AuthState.Loading
        viewModelScope.launch {
            _authState.value = repository.login(email, password)
        }
    }

    fun register(email: String, password: String) {
        _authState.value = AuthState.Loading
        viewModelScope.launch {
            _authState.value = repository.register(email, password)
        }
    }

    fun resetPassword(email: String) {
        _authState.value = AuthState.Loading
        viewModelScope.launch {
            _authState.value = repository.resetPassword(email)
        }
    }

    fun resetState() {
        _authState.value = AuthState.Idle
    }
}

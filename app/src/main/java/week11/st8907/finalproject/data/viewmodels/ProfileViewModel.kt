/**
 * ProfileViewModel.kt
 * -------------------------
 * ViewModel for user profile management.
 * Handles user data, progress tracking, and profile operations.
 */
package week11.st8907.finalproject.data.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import week11.st8907.finalproject.data.models.User
import week11.st8907.finalproject.data.repositories.UserRepository

class ProfileViewModel : ViewModel() {
    private val userRepository = UserRepository()

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    // Load user profile
    fun loadUserProfile(userId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            val result = userRepository.getUser(userId)
            if (result.isSuccess) {
                _currentUser.value = result.getOrThrow()
            } else {
                _errorMessage.value = "Failed to load user profile: ${result.exceptionOrNull()?.message}"
            }
            _isLoading.value = false
        }
    }

    // Update user profile
    fun updateUserProfile(user: User, onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            val result = userRepository.createOrUpdateUser(user)
            if (result.isSuccess) {
                _currentUser.value = user
                onSuccess()
            } else {
                _errorMessage.value = "Failed to update profile: ${result.exceptionOrNull()?.message}"
            }
            _isLoading.value = false
        }
    }

    // Update user progress
    fun updateUserProgress(userId: String, xpEarned: Int, onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            val result = userRepository.updateUserProgress(userId, xpEarned)
            if (result.isSuccess) {
                loadUserProfile(userId)
                onSuccess()
            } else {
                _errorMessage.value = "Failed to update progress: ${result.exceptionOrNull()?.message}"
            }
            _isLoading.value = false
        }
    }

    // Update user name
    fun updateUserName(userId: String, newName: String, onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            val currentUser = _currentUser.value
            if (currentUser != null) {
                val updatedUser = currentUser.copy(name = newName)
                val result = userRepository.createOrUpdateUser(updatedUser)
                if (result.isSuccess) {
                    _currentUser.value = updatedUser
                    onSuccess()
                } else {
                    _errorMessage.value = "Failed to update name: ${result.exceptionOrNull()?.message}"
                }
            } else {
                _errorMessage.value = "User not loaded"
            }
            _isLoading.value = false
        }
    }

    fun clearError() {
        _errorMessage.value = null
    }
}
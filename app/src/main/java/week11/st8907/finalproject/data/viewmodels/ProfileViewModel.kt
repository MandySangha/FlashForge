package week11.st8907.finalproject.data.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import week11.st8907.finalproject.data.models.User
import week11.st8907.finalproject.data.repositories.UserRepository

class ProfileViewModel(
    private val userRepository: UserRepository = UserRepository()
) : ViewModel() {

    private val auth = FirebaseAuth.getInstance()

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _updateSuccess = MutableStateFlow(false)
    val updateSuccess: StateFlow<Boolean> = _updateSuccess

    init {
        setupRealtimeUserListener()
    }

    // ---------------------------------------------------------------------
    // REAL-TIME USER LISTENER
    // ---------------------------------------------------------------------
    private fun setupRealtimeUserListener() {
        val uid = auth.currentUser?.uid ?: return

        userRepository.getUserRealtime(uid) { user ->
            _currentUser.value = user
        }
    }

    // ---------------------------------------------------------------------
    // LOAD USER FROM FIRESTORE (One-time load)
    // ---------------------------------------------------------------------
    fun loadUserProfile() {
        val uid = auth.currentUser?.uid ?: return

        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            val result = userRepository.getUser(uid)

            if (result.isSuccess) {
                _currentUser.value = result.getOrThrow()
            } else {
                _errorMessage.value = result.exceptionOrNull()?.message
            }

            _isLoading.value = false
        }
    }

    // ---------------------------------------------------------------------
    // UPDATE USER PROGRESS (Study sessions)
    // ---------------------------------------------------------------------
    fun updateUserProgress(xpEarned: Int, onSuccess: () -> Unit = {}) {
        val userId = auth.currentUser?.uid ?: return

        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            try {
                val result = userRepository.updateUserProgress(userId, xpEarned)
                if (result.isSuccess) {
                    onSuccess()
                } else {
                    _errorMessage.value = result.exceptionOrNull()?.message
                }
            } catch (e: Exception) {
                _errorMessage.value = "Failed to update progress: ${e.message}"
            }

            _isLoading.value = false
        }
    }

    // ---------------------------------------------------------------------
    // UPDATE USER PROFILE - FIXED VERSION
    // ---------------------------------------------------------------------
    fun updateUserProfile(
        name: String,
        onSuccess: () -> Unit = {}
    ) {
        val userAuth = auth.currentUser
            ?: run {
                _errorMessage.value = "User not authenticated"
                return
            }

        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            _updateSuccess.value = false

            try {
                val userId = userAuth.uid

                // Get current user to preserve other fields
                val currentResult = userRepository.getUser(userId)

                if (!currentResult.isSuccess) {
                    _errorMessage.value = "Failed to load user data: ${currentResult.exceptionOrNull()?.message}"
                    _isLoading.value = false
                    return@launch
                }

                val currentUser = currentResult.getOrThrow()
                val trimmedName = name.trim()

                if (trimmedName.isNotEmpty() && trimmedName != currentUser.name) {
                    try {
                        val profileUpdates = UserProfileChangeRequest.Builder()
                            .setDisplayName(trimmedName)
                            .build()
                        userAuth.updateProfile(profileUpdates).await()
                        println("Updated Auth display name to: $trimmedName")
                    } catch (authEx: Exception) {
                        println("Failed to update Auth profile: ${authEx.message}")
                    }
                }

                if (trimmedName.isNotEmpty() && trimmedName != currentUser.name) {
                    val updateResult = userRepository.updateUserName(userId, trimmedName)

                    if (updateResult.isSuccess) {
                        println("Updated Firestore name to: $trimmedName")

                        // Update local state with the new name
                        _currentUser.value = currentUser.copy(name = trimmedName)
                        _updateSuccess.value = true
                        onSuccess()
                    } else {
                        _errorMessage.value = "Failed to update profile: ${updateResult.exceptionOrNull()?.message}"
                    }
                } else {
                    _errorMessage.value = "Name unchanged or empty"
                }

            } catch (e: Exception) {
                _errorMessage.value = "Error updating profile: ${e.message}"
                println("Profile update error: ${e.message}")
            }

            _isLoading.value = false
        }
    }

    // ---------------------------------------------------------------------
    // CLEAR ERROR MESSAGE
    // ---------------------------------------------------------------------
    fun clearErrorMessage() {
        _errorMessage.value = null
    }

    // ---------------------------------------------------------------------
    // RESET UPDATE SUCCESS
    // ---------------------------------------------------------------------
    fun resetUpdateSuccess() {
        _updateSuccess.value = false
    }
}
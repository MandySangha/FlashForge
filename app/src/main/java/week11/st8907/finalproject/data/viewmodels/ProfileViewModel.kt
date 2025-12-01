package week11.st8907.finalproject.data.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import week11.st8907.finalproject.models.User
import week11.st8907.finalproject.repositories.UserRepository

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

    // Load user profile from Firestore
    fun loadUserProfile() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                val user = userRepository.getUser()
                _currentUser.value = user
            } catch (e: Exception) {
                _errorMessage.value = "Failed to load user: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Update user profile
    fun updateUserProfile(name: String, email: String, onSuccess: () -> Unit = {}) {
        val currentUserAuth = auth.currentUser
        if (currentUserAuth == null) {
            _errorMessage.value = "User not authenticated"
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            try {
                // Update Firebase Auth display name
                val profileUpdates = UserProfileChangeRequest.Builder()
                    .setDisplayName(name)
                    .build()
                currentUserAuth.updateProfile(profileUpdates).await()

                // Update email if changed
                if (email != currentUserAuth.email) {
                    currentUserAuth.updateEmail(email).await()
                }

                // Update Firestore
                val updatedUser = _currentUser.value?.copy(
                    name = name,
                    email = email
                ) ?: User(
                    userId = currentUserAuth.uid,
                    name = name,
                    email = email
                )

                userRepository.updateUser(updatedUser)
                _currentUser.value = updatedUser
                onSuccess()
            } catch (e: Exception) {
                _errorMessage.value = "Failed to update profile: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}

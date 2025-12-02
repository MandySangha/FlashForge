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

    // ---------------------------------------------------------------------
    // LOAD USER FROM FIRESTORE
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
    // UPDATE USER PROFILE (Auth + Firestore)
    // ---------------------------------------------------------------------
    fun updateUserProfile(
        name: String,
        email: String,
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

            try {
                // Update Firebase Auth display name
                val profileUpdates = UserProfileChangeRequest.Builder()
                    .setDisplayName(name)
                    .build()

                userAuth.updateProfile(profileUpdates).await()

                // Update Firebase Auth email
                if (email != userAuth.email) {
                    userAuth.updateEmail(email).await()
                }

                // Build updated user model
                val updatedUser = _currentUser.value?.copy(
                    name = name,
                    email = email
                ) ?: User(
                    userId = userAuth.uid,
                    name = name,
                    email = email
                )

                // Firestore update
                val repoResult = userRepository.createOrUpdateUser(updatedUser)

                if (repoResult.isSuccess) {
                    _currentUser.value = updatedUser
                    onSuccess()
                } else {
                    _errorMessage.value = repoResult.exceptionOrNull()?.message
                }

            } catch (e: Exception) {
                _errorMessage.value = "Failed to update profile: ${e.message}"
            }

            _isLoading.value = false
        }
    }
}

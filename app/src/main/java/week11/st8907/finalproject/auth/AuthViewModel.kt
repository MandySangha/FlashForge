package week11.st8907.finalproject.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import week11.st8907.finalproject.data.models.User
import week11.st8907.finalproject.data.repositories.UserRepository

class AuthViewModel(
    private val repository: AuthRepository = AuthRepository(),
    private val userRepo: UserRepository = UserRepository()
) : ViewModel() {

    private val auth = FirebaseAuth.getInstance()

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password

    private val _confirmPassword = MutableStateFlow("")
    val confirmPassword: StateFlow<String> = _confirmPassword

    fun updateEmail(value: String) { _email.value = value }
    fun updatePassword(value: String) { _password.value = value }
    fun updateConfirmPassword(value: String) { _confirmPassword.value = value }


    fun login() {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            val result = repository.login(email.value, password.value)

            if (result is AuthState.Success) {
                val uid = auth.currentUser!!.uid

                val user = User(
                    userId = uid,
                    email = email.value,
                    name = email.value.substringBefore("@")
                )

                userRepo.createOrUpdateUser(user)
            }

            _authState.value = result
        }
    }


    fun register() {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            val result = repository.register(email.value, password.value)

            if (result is AuthState.Success) {
                val uid = auth.currentUser!!.uid

                val user = User(
                    userId = uid,
                    email = email.value,
                    name = email.value.substringBefore("@")
                )

                userRepo.createOrUpdateUser(user)
            }

            _authState.value = result
        }
    }

    fun resetPassword() {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            val result = repository.resetPassword(email.value)
            _authState.value = result
        }
    }

    fun clearState() {
        _authState.value = AuthState.Idle
    }
}

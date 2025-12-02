package week11.st8907.finalproject.data.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import week11.st8907.finalproject.data.models.User
import week11.st8907.finalproject.data.repositories.UserRepository

class UserViewModel(
    private val repo: UserRepository = UserRepository()
) : ViewModel() {

    private val auth = FirebaseAuth.getInstance()

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user

    init {
        val uid = auth.currentUser?.uid
        if (uid != null) {
            repo.getUserRealtime(uid) { fetched ->
                _user.value = fetched
            }
        }
    }

    fun refreshUser() {
        val uid = auth.currentUser?.uid ?: return

        viewModelScope.launch {
            val result = repo.getUser(uid)
            if (result.isSuccess) {
                _user.value = result.getOrThrow()
            }
        }
    }
}

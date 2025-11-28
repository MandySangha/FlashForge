package week11.st8907.finalproject.repositories

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import week11.st8907.finalproject.models.User

class UserRepository(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
) {

    private fun userDoc() =
        db.collection("users").document(auth.currentUser!!.uid)

    // Create user with Long timestamps (NO Timestamp object)
    suspend fun createUserIfNotExists(email: String) {
        val doc = userDoc().get().await()
        if (!doc.exists()) {
            val user = User(
                userId = auth.currentUser!!.uid,
                email = email,
                name = email.substringBefore("@"),
                createdAt = System.currentTimeMillis()   // IMPORTANT FIX
            )
            userDoc().set(user).await()
        }
    }

    suspend fun getUser(): User? {
        return try {
            userDoc().get().await().toObject(User::class.java)
        } catch (e: Exception) {
            null
        }
    }

    fun getUserRealtime(listener: (User?) -> Unit) {
        userDoc().addSnapshotListener { snapshot, _ ->
            listener(snapshot?.toObject(User::class.java))
        }
    }

    suspend fun updateUser(user: User) {
        userDoc().set(user).await()
    }
}

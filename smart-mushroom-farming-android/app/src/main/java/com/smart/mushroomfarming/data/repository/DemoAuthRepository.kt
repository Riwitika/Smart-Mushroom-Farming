package com.smart.mushroomfarming.data.repository

import android.content.Context
import com.smart.mushroomfarming.domain.model.User
import com.smart.mushroomfarming.domain.repository.AuthRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DemoAuthRepository @Inject constructor(
    @ApplicationContext private val context: Context
) : AuthRepository {

    private val prefs = context.getSharedPreferences("demo_auth_prefs", Context.MODE_PRIVATE)

    override suspend fun login(email: String, password: String): User {
        delay(1000) // Simulate network delay
        
        // Retrieve simulated accounts
        val savedPassword = prefs.getString("pwd_$email", null)
        val savedName = prefs.getString("name_$email", "Mushroom Farmer").orEmpty()
        val savedUid = prefs.getString("uid_$email", "demo-uid-12345").orEmpty()

        if (savedPassword == null) {
            // Default user fallback for easy demo testing
            if (email == "test@example.com" && password == "Password123!") {
                saveSession("demo-uid-123", "test@example.com", "Demo Farmer")
                return User("demo-uid-123", "test@example.com", "Demo Farmer")
            }
            throw Exception("User not found. Please register first.")
        }

        if (savedPassword != password) {
            throw Exception("Invalid credentials. Please check your password.")
        }

        saveSession(savedUid, email, savedName)
        return User(savedUid, email, savedName)
    }

    override suspend fun register(email: String, password: String, name: String): User {
        delay(1000) // Simulate network delay

        // Check if user already exists
        if (prefs.contains("pwd_$email") || (email == "test@example.com")) {
            throw Exception("User already registered. Please login.")
        }

        val uid = "uid-${System.currentTimeMillis()}"

        // Save simulated account
        prefs.edit().apply {
            putString("pwd_$email", password)
            putString("name_$email", name)
            putString("uid_$email", uid)
            apply()
        }

        saveSession(uid, email, name)
        return User(uid, email, name)
    }

    override suspend fun forgotPassword(email: String) {
        delay(1000)
        // Check if user exists
        if (!prefs.contains("pwd_$email") && email != "test@example.com") {
            throw Exception("Email address not found. Please register.")
        }
    }

    override suspend fun logout() {
        prefs.edit().apply {
            remove("current_uid")
            remove("current_email")
            remove("current_name")
            putBoolean("is_logged_in", false)
            apply()
        }
    }

    override fun getCurrentUser(): User? {
        val isLoggedIn = prefs.getBoolean("is_logged_in", false)
        if (!isLoggedIn) return null

        val uid = prefs.getString("current_uid", null) ?: return null
        val email = prefs.getString("current_email", "") ?: ""
        val name = prefs.getString("current_name", "Farmer")

        return User(uid, email, name)
    }

    override fun isUserLoggedIn(): Boolean {
        return prefs.getBoolean("is_logged_in", false)
    }

    private fun saveSession(uid: String, email: String, name: String) {
        prefs.edit().apply {
            putString("current_uid", uid)
            putString("current_email", email)
            putString("current_name", name)
            putBoolean("is_logged_in", true)
            apply()
        }
    }
}

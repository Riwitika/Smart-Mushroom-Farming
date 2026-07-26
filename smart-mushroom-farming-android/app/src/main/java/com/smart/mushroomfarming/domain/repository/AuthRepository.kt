package com.smart.mushroomfarming.domain.repository

import com.smart.mushroomfarming.domain.model.User

interface AuthRepository {
    suspend fun login(email: String, password: String): User
    suspend fun register(email: String, password: String, name: String): User
    suspend fun forgotPassword(email: String)
    suspend fun logout()
    fun getCurrentUser(): User?
    fun isUserLoggedIn(): Boolean
}

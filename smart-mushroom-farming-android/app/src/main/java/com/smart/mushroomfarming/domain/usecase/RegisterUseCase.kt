package com.smart.mushroomfarming.domain.usecase

import com.smart.mushroomfarming.domain.model.User
import com.smart.mushroomfarming.domain.repository.AuthRepository
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String, name: String): User {
        return repository.register(email.trim(), password, name.trim())
    }
}

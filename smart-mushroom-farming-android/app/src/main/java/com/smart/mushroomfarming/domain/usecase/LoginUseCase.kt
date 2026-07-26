package com.smart.mushroomfarming.domain.usecase

import com.smart.mushroomfarming.domain.model.User
import com.smart.mushroomfarming.domain.repository.AuthRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String): User {
        return repository.login(email.trim(), password)
    }
}

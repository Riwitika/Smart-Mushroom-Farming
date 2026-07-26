package com.smart.mushroomfarming.domain.usecase

import com.smart.mushroomfarming.domain.repository.AuthRepository
import javax.inject.Inject

class ForgotPasswordUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(email: String) {
        repository.forgotPassword(email.trim())
    }
}

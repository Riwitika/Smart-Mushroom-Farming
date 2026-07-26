package com.smart.mushroomfarming.domain.usecase

import com.smart.mushroomfarming.domain.model.User
import com.smart.mushroomfarming.domain.repository.AuthRepository
import javax.inject.Inject

class GetCurrentUserUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    operator fun invoke(): User? {
        return repository.getCurrentUser()
    }
}

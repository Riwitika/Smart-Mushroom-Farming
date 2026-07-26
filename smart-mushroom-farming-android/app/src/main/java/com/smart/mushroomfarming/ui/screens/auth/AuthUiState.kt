package com.smart.mushroomfarming.ui.screens.auth

import com.smart.mushroomfarming.utils.Resource

data class AuthUiState(
    val email: String = "",
    val emailError: String? = null,
    val password: String = "",
    val passwordError: String? = null,
    val name: String = "",
    val nameError: String? = null,
    val confirmPassword: String = "",
    val confirmPasswordError: String? = null,
    val authResult: Resource<Unit>? = null
)

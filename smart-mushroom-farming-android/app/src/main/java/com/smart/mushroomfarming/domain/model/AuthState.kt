package com.smart.mushroomfarming.domain.model

sealed class AuthState<out T> {
    data object Idle : AuthState<Nothing>()
    data object Loading : AuthState<Nothing>()
    data class Success<out T>(val data: T) : AuthState<T>()
    data class Error(val message: String) : AuthState<Nothing>()
}

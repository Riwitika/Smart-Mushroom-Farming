package com.smart.mushroomfarming.utils

sealed class Resource<out T> {
    data class Success<out T>(val data: T) : Resource<T>()
    data class Error(val exception: Throwable, val message: String? = null) : Resource<Nothing>()
    data object Loading : Resource<Nothing>()
}

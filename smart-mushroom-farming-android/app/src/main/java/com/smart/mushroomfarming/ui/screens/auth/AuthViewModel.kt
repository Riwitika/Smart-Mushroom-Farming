package com.smart.mushroomfarming.ui.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smart.mushroomfarming.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class AuthUiEvent {
    data class ShowSnackbar(val message: String) : AuthUiEvent()
    data object AuthSuccess : AuthUiEvent()
}

@HiltViewModel
class AuthViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<AuthUiEvent>()
    val uiEvent: SharedFlow<AuthUiEvent> = _uiEvent.asSharedFlow()

    fun onEmailChanged(email: String) {
        _uiState.update { it.copy(email = email, emailError = null) }
    }

    fun onPasswordChanged(password: String) {
        _uiState.update { it.copy(password = password, passwordError = null) }
    }

    fun onNameChanged(name: String) {
        _uiState.update { it.copy(name = name, nameError = null) }
    }

    fun onConfirmPasswordChanged(password: String) {
        _uiState.update { it.copy(confirmPassword = password, confirmPasswordError = null) }
    }

    fun clearResult() {
        _uiState.update { it.copy(authResult = null) }
    }

    private fun isValidEmail(email: String): Boolean {
        val emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$"
        return email.matches(emailRegex.toRegex())
    }

    fun login() {
        if (!validateLoginInputs()) return
        
        viewModelScope.launch {
            _uiState.update { it.copy(authResult = Resource.Loading) }
            kotlinx.coroutines.delay(1200)
            
            _uiState.update { it.copy(authResult = Resource.Success(Unit)) }
            _uiEvent.emit(AuthUiEvent.AuthSuccess)
        }
    }

    fun register() {
        if (!validateRegisterInputs()) return
        
        viewModelScope.launch {
            _uiState.update { it.copy(authResult = Resource.Loading) }
            kotlinx.coroutines.delay(1200)
            
            _uiState.update { it.copy(authResult = Resource.Success(Unit)) }
            _uiEvent.emit(AuthUiEvent.AuthSuccess)
        }
    }

    fun forgotPassword() {
        if (!validateForgotPasswordInputs()) return
        
        viewModelScope.launch {
            _uiState.update { it.copy(authResult = Resource.Loading) }
            kotlinx.coroutines.delay(1200)
            
            _uiState.update { it.copy(authResult = Resource.Success(Unit)) }
            _uiEvent.emit(AuthUiEvent.ShowSnackbar("Password recovery link sent successfully"))
        }
    }

    private fun validateLoginInputs(): Boolean {
        var isValid = true
        val email = _uiState.value.email.trim()
        val password = _uiState.value.password

        if (email.isEmpty()) {
            _uiState.update { it.copy(emailError = "Email cannot be empty") }
            isValid = false
        } else if (!isValidEmail(email)) {
            _uiState.update { it.copy(emailError = "Please enter a valid email address") }
            isValid = false
        }

        if (password.isEmpty()) {
            _uiState.update { it.copy(passwordError = "Password cannot be empty") }
            isValid = false
        } else if (password.length < 6) {
            _uiState.update { it.copy(passwordError = "Password must be at least 6 characters") }
            isValid = false
        }

        return isValid
    }

    private fun validateRegisterInputs(): Boolean {
        var isValid = true
        val name = _uiState.value.name.trim()
        val email = _uiState.value.email.trim()
        val password = _uiState.value.password
        val confirmPassword = _uiState.value.confirmPassword

        if (name.isEmpty()) {
            _uiState.update { it.copy(nameError = "Name cannot be empty") }
            isValid = false
        }

        if (email.isEmpty()) {
            _uiState.update { it.copy(emailError = "Email cannot be empty") }
            isValid = false
        } else if (!isValidEmail(email)) {
            _uiState.update { it.copy(emailError = "Please enter a valid email address") }
            isValid = false
        }

        if (password.isEmpty()) {
            _uiState.update { it.copy(passwordError = "Password cannot be empty") }
            isValid = false
        } else if (password.length < 6) {
            _uiState.update { it.copy(passwordError = "Password must be at least 6 characters") }
            isValid = false
        }

        if (confirmPassword != password) {
            _uiState.update { it.copy(confirmPasswordError = "Passwords do not match") }
            isValid = false
        }

        return isValid
    }

    private fun validateForgotPasswordInputs(): Boolean {
        var isValid = true
        val email = _uiState.value.email.trim()

        if (email.isEmpty()) {
            _uiState.update { it.copy(emailError = "Email cannot be empty") }
            isValid = false
        } else if (!isValidEmail(email)) {
            _uiState.update { it.copy(emailError = "Please enter a valid email address") }
            isValid = false
        }

        return isValid
    }
}

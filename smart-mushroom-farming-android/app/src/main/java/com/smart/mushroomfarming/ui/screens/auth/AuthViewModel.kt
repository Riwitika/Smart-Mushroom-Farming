package com.smart.mushroomfarming.ui.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smart.mushroomfarming.domain.model.AuthState
import com.smart.mushroomfarming.domain.model.User
import com.smart.mushroomfarming.domain.usecase.ForgotPasswordUseCase
import com.smart.mushroomfarming.domain.usecase.GetCurrentUserUseCase
import com.smart.mushroomfarming.domain.usecase.LoginUseCase
import com.smart.mushroomfarming.domain.usecase.LogoutUseCase
import com.smart.mushroomfarming.domain.usecase.RegisterUseCase
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
class AuthViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val registerUseCase: RegisterUseCase,
    private val forgotPasswordUseCase: ForgotPasswordUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val logoutUseCase: LogoutUseCase
) : ViewModel() {

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
        _uiState.update { it.copy(authResult = AuthState.Idle) }
    }

    fun isUserLoggedIn(): Boolean {
        return getCurrentUserUseCase() != null
    }

    fun getCurrentUser(): User? {
        return getCurrentUserUseCase()
    }

    private fun isValidEmail(email: String): Boolean {
        val emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$"
        return email.matches(emailRegex.toRegex())
    }

    private fun isValidPassword(password: String): Boolean {
        if (password.length < 8) return false
        val hasUppercase = password.any { it.isUpperCase() }
        val hasLowercase = password.any { it.isLowerCase() }
        val hasNumber = password.any { it.isDigit() }
        val hasSpecial = password.any { !it.isLetterOrDigit() }
        return hasUppercase && hasLowercase && hasNumber && hasSpecial
    }

    fun login() {
        if (!validateLoginInputs()) return
        
        viewModelScope.launch {
            _uiState.update { it.copy(authResult = AuthState.Loading) }
            try {
                loginUseCase(_uiState.value.email, _uiState.value.password)
                _uiState.update { it.copy(authResult = AuthState.Success(Unit)) }
                _uiEvent.emit(AuthUiEvent.AuthSuccess)
            } catch (e: Exception) {
                _uiState.update { it.copy(authResult = AuthState.Error(getErrorMessage(e))) }
            }
        }
    }

    fun register() {
        if (!validateRegisterInputs()) return
        
        viewModelScope.launch {
            _uiState.update { it.copy(authResult = AuthState.Loading) }
            try {
                registerUseCase(
                    _uiState.value.email,
                    _uiState.value.password,
                    _uiState.value.name
                )
                _uiState.update { it.copy(authResult = AuthState.Success(Unit)) }
                _uiEvent.emit(AuthUiEvent.AuthSuccess)
            } catch (e: Exception) {
                _uiState.update { it.copy(authResult = AuthState.Error(getErrorMessage(e))) }
            }
        }
    }

    fun forgotPassword() {
        if (!validateForgotPasswordInputs()) return
        
        viewModelScope.launch {
            _uiState.update { it.copy(authResult = AuthState.Loading) }
            try {
                forgotPasswordUseCase(_uiState.value.email)
                _uiState.update { it.copy(authResult = AuthState.Success(Unit)) }
                _uiEvent.emit(AuthUiEvent.ShowSnackbar("Password recovery link sent successfully"))
            } catch (e: Exception) {
                _uiState.update { it.copy(authResult = AuthState.Error(getErrorMessage(e))) }
            }
        }
    }

    fun logout(onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                logoutUseCase()
                onSuccess()
            } catch (e: Exception) {
                // Non-critical, simply redirect or handle error locally
                onSuccess()
            }
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
        } else if (!isValidPassword(password)) {
            _uiState.update { 
                it.copy(passwordError = "Password must be at least 8 characters, and contain uppercase, lowercase, number, and special character") 
            }
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

    private fun getErrorMessage(e: Throwable): String {
        val msg = e.message ?: ""
        return when {
            msg.contains("network", ignoreCase = true) || msg.contains("route", ignoreCase = true) || msg.contains("socket", ignoreCase = true) -> 
                "Network unavailable. Please check your internet connection."
            msg.contains("password is invalid", ignoreCase = true) || msg.contains("weak", ignoreCase = true) ->
                "Weak password. Password must contain at least 8 characters with letters, numbers, and special symbols."
            msg.contains("user not found", ignoreCase = true) || msg.contains("no user record", ignoreCase = true) || msg.contains("wrong", ignoreCase = true) || msg.contains("invalid credentials", ignoreCase = true) ->
                "Wrong credentials. Please check your email and password."
            msg.contains("invalid email", ignoreCase = true) || msg.contains("badly formatted", ignoreCase = true) ->
                "Invalid email format."
            msg.isNotEmpty() -> msg
            else -> "An unknown error occurred. Please try again."
        }
    }
}

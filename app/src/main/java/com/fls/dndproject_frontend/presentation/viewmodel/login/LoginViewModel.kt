package com.fls.dndproject_frontend.presentation.viewmodel.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fls.dndproject_frontend.domain.usecase.users.ListUsersUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.mindrot.jbcrypt.BCrypt
import android.util.Log

class LoginViewModel(
    private val listUsersUseCase: ListUsersUseCase
) : ViewModel() {

    private val _username = MutableStateFlow("")
    val username: StateFlow<String> = _username

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password

    private val _snackbarMessage = MutableStateFlow<String?>(null)
    val snackbarMessage: StateFlow<String?> = _snackbarMessage

    private val _loginSuccess = MutableStateFlow(false)
    val loginSuccess: StateFlow<Boolean> = _loginSuccess

    fun setUsername(username: String) {
        _username.value = username
    }

    fun setPassword(password: String) {
        _password.value = password
    }

    fun clearSnackbarMessage() {
        _snackbarMessage.value = null
    }

    fun login() {
        _snackbarMessage.value = null
        _loginSuccess.value = false

        if (_username.value.isBlank() || _password.value.isBlank()) {
            _snackbarMessage.value = "Por favor, introduce tu nombre de usuario y contraseña."
            return
        }

        viewModelScope.launch {
            try {
                val allUsers = listUsersUseCase.invoke().first()
                Log.d("LoginViewModel", "Usuarios obtenidos para login: $allUsers")

                val userToLogin = allUsers.firstOrNull { it.username.equals(_username.value, ignoreCase = true) }

                if (userToLogin == null) {
                    _snackbarMessage.value = "Nombre de usuario o contraseña incorrectos."
                    return@launch
                }

                val isPasswordCorrect = BCrypt.checkpw(_password.value, userToLogin.password)
                Log.d("LoginViewModel", "Intento de contraseña: ${_password.value}")
                Log.d("LoginViewModel", "Hash almacenado: ${userToLogin.password}")
                Log.d("LoginViewModel", "Contraseña correcta: $isPasswordCorrect")

                if (isPasswordCorrect) {
                    _snackbarMessage.value = "¡Bienvenido de nuevo, ${userToLogin.username}!"
                    _loginSuccess.value = true
                } else {
                    _snackbarMessage.value = "Nombre de usuario o contraseña incorrectos."
                }

            } catch (e: Exception) {
                _snackbarMessage.value = "Error al intentar iniciar sesión: ${e.localizedMessage}"
                Log.e("LoginViewModel", "Error during login process", e)
            }
        }
    }
}
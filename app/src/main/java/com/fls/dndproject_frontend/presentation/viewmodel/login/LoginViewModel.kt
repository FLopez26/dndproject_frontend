package com.fls.dndproject_frontend.presentation.viewmodel.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fls.dndproject_frontend.domain.usecase.users.ListUsersUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.mindrot.jbcrypt.BCrypt

class LoginViewModel(
    private val listUsersUseCase: ListUsersUseCase
) : ViewModel() {

    private val _username = MutableStateFlow("")
    val username: StateFlow<String> = _username

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password

    private val _snackbarMessage = MutableStateFlow<String?>(null)
    val snackbarMessage: StateFlow<String?> = _snackbarMessage

    private val _navigateToHomeWithUserId = MutableSharedFlow<Int>()
    val navigateToHomeWithUserId: SharedFlow<Int> = _navigateToHomeWithUserId.asSharedFlow()

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

        if (_username.value.isBlank() || _password.value.isBlank()) {
            _snackbarMessage.value = "Por favor, introduce tu nombre de usuario y contraseña."
            return
        }

        viewModelScope.launch {
            try {
                val allUsers = listUsersUseCase.invoke().first()

                val userToLogin = allUsers.firstOrNull { it.username.equals(_username.value, ignoreCase = true) }

                if (userToLogin == null) {
                    _snackbarMessage.value = "Nombre de usuario o contraseña incorrectos."
                    return@launch
                }

                val isPasswordCorrect = BCrypt.checkpw(_password.value, userToLogin.password)

                if (isPasswordCorrect) {
                    _snackbarMessage.value = "¡Bienvenido de nuevo, ${userToLogin.username}!"
                    userToLogin.userId?.let { id ->
                        _navigateToHomeWithUserId.emit(id)
                    } ?: run {
                        _snackbarMessage.value = "Error interno: ID de usuario no encontrado."
                    }
                } else {
                    _snackbarMessage.value = "Nombre de usuario o contraseña incorrectos."
                }

            } catch (e: Exception) {
                _snackbarMessage.value = "Error al intentar iniciar sesión: ${e.localizedMessage}"
            }
        }
    }
}
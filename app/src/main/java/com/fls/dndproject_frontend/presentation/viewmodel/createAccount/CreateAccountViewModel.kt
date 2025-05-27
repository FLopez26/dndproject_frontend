package com.fls.dndproject_frontend.presentation.viewmodel.createAccount

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fls.dndproject_frontend.domain.model.User
import com.fls.dndproject_frontend.domain.usecase.users.CreateAccountUseCase
import com.fls.dndproject_frontend.domain.usecase.users.ListUsersUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.mindrot.jbcrypt.BCrypt

class CreateAccountViewModel(
    val createAccountUseCase: CreateAccountUseCase,
    val listUsersUseCase: ListUsersUseCase
): ViewModel() {

    private val _username = MutableStateFlow("")
    val username: StateFlow<String> = _username

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password

    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message

    fun setUsername(username: String) {
        this._username.value = username
    }

    fun setEmail(email: String) {
        this._email.value = email
    }

    fun setPassword(password: String) {
        this._password.value = password
    }

    private fun hashPassword(plainTextPassword: String): String {
        val SALT_ROUNDS = 12
        return BCrypt.hashpw(plainTextPassword, BCrypt.gensalt(SALT_ROUNDS))
    }

    private fun isValidEmail(email: String): Boolean {
        val emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$".toRegex()
        return emailRegex.matches(email)
    }

    fun createAccount(confirmPassword: String) {
        _message.value = null

        if (_username.value.isBlank() || _email.value.isBlank() || _password.value.isBlank() || confirmPassword.isBlank()) {
            _message.value = "Todos los campos son obligatorios."
            return
        }

        if (_username.value.length > 16) {
            _message.value = "El nombre de usuario no puede exceder los 16 caracteres."
            return
        }

        if (!isValidEmail(_email.value)) {
            _message.value = "El correo electrónico no tiene un formato válido."
            return
        }

        if (_password.value != confirmPassword) {
            _message.value = "Las contraseñas no coinciden."
            return
        }


        viewModelScope.launch {
            try {
                val existingUsers = listUsersUseCase.invoke().first()

                val usernameExists = existingUsers.any { it.username.equals(_username.value, ignoreCase = true) }
                if (usernameExists) {
                    _message.value = "El nombre de usuario '${_username.value}' ya está en uso."
                    return@launch
                }

                val emailExists = existingUsers.any { it.email.equals(_email.value, ignoreCase = true) }
                if (emailExists) {
                    _message.value = "El correo electrónico '${_email.value}' ya está registrado."
                    return@launch
                }

                val hashedPassword = hashPassword(_password.value)


                createAccountUseCase(User(null, _username.value, _email.value, hashedPassword))

                _message.value = "Cuenta creada exitosamente."
                _username.value = ""
                _email.value = ""
                _password.value = ""

            } catch (e: Exception) {
                _message.value = "Error en el proceso de creación: ${e.localizedMessage}"
            }
        }
    }
}
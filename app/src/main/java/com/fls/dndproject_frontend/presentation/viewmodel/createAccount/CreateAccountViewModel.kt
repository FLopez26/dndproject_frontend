package com.fls.dndproject_frontend.presentation.viewmodel.createAccount

import android.util.Log // Asegúrate de tener esta importación
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
        Log.d("CreateAccountViewModel", "createAccount: Invocada la función createAccount().")

        if (_username.value.isBlank() || _email.value.isBlank() || _password.value.isBlank() || confirmPassword.isBlank()) {
            _message.value = "Todos los campos son obligatorios."
            Log.d("CreateAccountViewModel", "createAccount: Validación fallida - Campos obligatorios vacíos.")
            return
        }

        if (_username.value.length > 16) {
            _message.value = "El nombre de usuario no puede exceder los 16 caracteres."
            Log.d("CreateAccountViewModel", "createAccount: Validación fallida - Nombre de usuario demasiado largo (${_username.value.length} > 16).")
            return
        }

        if (!isValidEmail(_email.value)) {
            _message.value = "El correo electrónico no tiene un formato válido."
            Log.d("CreateAccountViewModel", "createAccount: Validación fallida - Formato de correo electrónico inválido.")
            return
        }

        if (_password.value != confirmPassword) {
            _message.value = "Las contraseñas no coinciden."
            Log.d("CreateAccountViewModel", "createAccount: Validación fallida - Las contraseñas no coinciden.")
            return
        }

        Log.d("CreateAccountViewModel", "createAccount: Todas las validaciones iniciales pasadas. Lanzando coroutine para operaciones de BD.")

        viewModelScope.launch {
            Log.d("CreateAccountViewModel", "Coroutine: Iniciando bloque suspendido de operaciones de BD.")
            try {
                Log.d("CreateAccountViewModel", "createAccount - Valores de entrada:")
                Log.d("CreateAccountViewModel", "  Username StateFlow: '${_username.value}'")
                Log.d("CreateAccountViewModel", "  Email StateFlow:    '${_email.value}'")
                Log.d("CreateAccountViewModel", "  Password StateFlow: (longitud: ${_password.value.length})")
                Log.d("CreateAccountViewModel", "  Confirm Password:   (longitud: ${confirmPassword.length})")
                Log.d("CreateAccountViewModel", "Coroutine: Intentando obtener usuarios existentes con listUsersUseCase.invoke().first()...")
                val existingUsers = listUsersUseCase.invoke().first()
                Log.d("CreateAccountViewModel", "Coroutine: Usuarios existentes obtenidos. Cantidad: ${existingUsers.size}. Detalles: $existingUsers")

                val usernameExists = existingUsers.any { it.username.equals(_username.value, ignoreCase = true) }
                if (usernameExists) {
                    _message.value = "El nombre de usuario '${_username.value}' ya está en uso."
                    Log.d("CreateAccountViewModel", "Coroutine: Validación de unicidad fallida - Nombre de usuario ya existe.")
                    return@launch
                }

                val emailExists = existingUsers.any { it.email.equals(_email.value, ignoreCase = true) }
                if (emailExists) {
                    _message.value = "El correo electrónico '${_email.value}' ya está registrado."
                    Log.d("CreateAccountViewModel", "Coroutine: Validación de unicidad fallida - Correo electrónico ya existe.")
                    return@launch
                }

                Log.d("CreateAccountViewModel", "Coroutine: Validaciones de unicidad pasadas. Procediendo a hashear contraseña y crear usuario.")
                val hashedPassword = hashPassword(_password.value)
                Log.d("CreateAccountViewModel", "Coroutine: Contraseña hasheada. Hash: $hashedPassword")


                Log.d("CreateAccountViewModel", "Coroutine: Intentando crear usuario con createAccountUseCase...")
                createAccountUseCase(User(0, _username.value, _email.value, hashedPassword))
                Log.d("CreateAccountViewModel", "Coroutine: createAccountUseCase ejecutado exitosamente.")

                _message.value = "Cuenta creada exitosamente."
                _username.value = ""
                _email.value = ""
                _password.value = ""
                Log.d("CreateAccountViewModel", "createAccount: Cuenta creada y campos limpiados.")

            } catch (e: Exception) {
                _message.value = "Error en el proceso de creación: ${e.localizedMessage}"
                Log.e("CreateAccountViewModel", "Coroutine: ERROR - Excepción durante el proceso de creación: ${e.localizedMessage}", e)
            }
        }
    }
}
// presentation/viewmodel/myCharacters/MyCharactersViewModel.kt
package com.fls.dndproject_frontend.presentation.viewmodel.myCharacters

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fls.dndproject_frontend.domain.usecase.characters.ListCharactersByUserUseCase
import com.fls.dndproject_frontend.domain.usecase.users.ListUsersUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import com.fls.dndproject_frontend.domain.model.Characters


class MyCharactersViewModel(
    private val listCharactersByUserUseCase: ListCharactersByUserUseCase,
    private val listUsersUseCase: ListUsersUseCase
) : ViewModel() {

    private val _usernameDisplay = MutableStateFlow<String?>("Cargando...")
    val usernameDisplay: StateFlow<String?> = _usernameDisplay.asStateFlow()

    private val _characters = MutableStateFlow<List<Characters>>(emptyList())
    val characters: StateFlow<List<Characters>> = _characters.asStateFlow()

    private val _hasCharacters = MutableStateFlow<Boolean?>(null)
    val hasCharacters: StateFlow<Boolean?> = _hasCharacters.asStateFlow()

    private val _uiMessage = MutableStateFlow<String?>(null)
    val uiMessage: StateFlow<String?> = _uiMessage.asStateFlow()

    fun loadUserDataAndCharacters(userId: Int?) {
        _hasCharacters.value = null
        _characters.value = emptyList()

        if (userId == null) {
            _uiMessage.value = "Error: ID de usuario no proporcionado."
            _usernameDisplay.value = "Error"
            _hasCharacters.value = false
            return
        }

        viewModelScope.launch {
            try {
                _uiMessage.value = "Cargando datos..."

                val allUsers = listUsersUseCase.invoke().first()

                if (allUsers.isEmpty()) {
                    _uiMessage.value = "No se encontraron usuarios en la base de datos."
                    _usernameDisplay.value = "Usuario No Encontrado"
                    _hasCharacters.value = false
                    return@launch
                }

                val currentUser = allUsers.firstOrNull { it.userId == userId }

                if (currentUser != null) {
                    _usernameDisplay.value = currentUser.username
                } else {
                    _usernameDisplay.value = "Usuario Desconocido"
                    _uiMessage.value = "Error: El usuario de sesión no fue encontrado."
                    _hasCharacters.value = false
                    return@launch
                }

                listCharactersByUserUseCase.invoke(userId).collect { it ->
                    _characters.value = it
                    _uiMessage.value = null


                    if (it.isEmpty()) {
                        _uiMessage.value = "No tienes personajes aún."
                        _hasCharacters.value = false
                    } else {
                        _hasCharacters.value = true
                    }
                }

            } catch (e: Exception) {
                _uiMessage.value = "Error al cargar datos: ${e.localizedMessage}"
                _usernameDisplay.value = "Error de Carga"
                _characters.value = emptyList()
                _hasCharacters.value = false
            }
        }
    }
    fun clearUiMessage() {
        _uiMessage.value = null
    }
}
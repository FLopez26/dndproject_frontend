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

    private val _characters = MutableStateFlow<List<Characters>>(emptyList())
    val characters: StateFlow<List<Characters>> = _characters.asStateFlow()

    private val _hasCharacters = MutableStateFlow<Boolean?>(null)
    val hasCharacters: StateFlow<Boolean?> = _hasCharacters.asStateFlow()


    fun loadUserDataAndCharacters(userId: Int?) {
        _hasCharacters.value = null
        _characters.value = emptyList()

        if (userId == null) {
            _hasCharacters.value = false
            return
        }

        viewModelScope.launch {
            try {
                val allUsers = listUsersUseCase.invoke().first()

                if (allUsers.isEmpty()) {
                    _hasCharacters.value = false
                    return@launch
                }

                val currentUser = allUsers.firstOrNull { it.userId == userId }

                if (currentUser == null) {
                    _hasCharacters.value = false
                    return@launch
                }

                listCharactersByUserUseCase.invoke(userId).collect { it ->
                    _characters.value = it

                    if (it.isEmpty()) {
                        _hasCharacters.value = false
                    } else {
                        _hasCharacters.value = true
                    }
                }

            } catch (e: Exception) {
                _characters.value = emptyList()
                _hasCharacters.value = false
            }
        }
    }
}
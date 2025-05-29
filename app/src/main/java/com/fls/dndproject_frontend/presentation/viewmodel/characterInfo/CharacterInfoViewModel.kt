package com.fls.dndproject_frontend.presentation.viewmodel.characterInfo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fls.dndproject_frontend.domain.model.Characters
import com.fls.dndproject_frontend.domain.usecase.characters.CharactersInfoUseCase
import com.fls.dndproject_frontend.domain.usecase.characters.UpdateCharacterUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CharacterInfoViewModel(
    private val charactersInfoUseCase: CharactersInfoUseCase,
    private val updateCharacterUseCase: UpdateCharacterUseCase
) : ViewModel() {

    private val _character = MutableStateFlow<Characters?>(null)
    val character: StateFlow<Characters?> = _character

    fun loadCharacterDetails(characterId: Int) {
        viewModelScope.launch {
            _character.value = null
            val result = charactersInfoUseCase(characterId)
            result.onSuccess { characterData ->
                _character.value = characterData
            }.onFailure { exception ->
                _character.value = null
            }
        }
    }

    fun updateCharacter() {
        val currentCharacter = _character.value ?: return
        val characterId = currentCharacter.characterId ?: return

        // Calcula el nuevo estado de isPublic
        val newIsPublic = !(currentCharacter.isPublic ?: false)

        viewModelScope.launch {
            val updatedCharacter = currentCharacter.copy(isPublic = newIsPublic)

            val result = updateCharacterUseCase(characterId, updatedCharacter)

            result.onSuccess { updatedData ->
                _character.value = updatedData
                println("Personaje actualizado a isPublic = $newIsPublic")
            }.onFailure { exception ->
                // TODO: Manejar el error de actualización, quizás con un Toast
                println("Error al actualizar la publicidad del personaje: ${exception.message}")
            }
        }
    }

}
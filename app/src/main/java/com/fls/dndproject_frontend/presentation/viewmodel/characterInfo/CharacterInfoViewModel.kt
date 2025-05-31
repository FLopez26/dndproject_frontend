package com.fls.dndproject_frontend.presentation.viewmodel.characterInfo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fls.dndproject_frontend.domain.model.Characters
import com.fls.dndproject_frontend.domain.usecase.characters.CharactersInfoUseCase
import com.fls.dndproject_frontend.domain.usecase.characters.DeleteCharacterUseCase
import com.fls.dndproject_frontend.domain.usecase.characters.UpdateCharacterUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CharacterInfoViewModel(
    private val charactersInfoUseCase: CharactersInfoUseCase,
    private val updateCharacterUseCase: UpdateCharacterUseCase,
    private val deleteCharacterUseCase: DeleteCharacterUseCase
) : ViewModel() {

    private val _character = MutableStateFlow<Characters?>(null)
    val character: StateFlow<Characters?> = _character

    private val _characterDeletedSuccessfully = MutableStateFlow(false)
    val characterDeletedSuccessfully: StateFlow<Boolean> = _characterDeletedSuccessfully

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

        val newIsPublic = !(currentCharacter.isPublic ?: false)

        viewModelScope.launch {
            val updatedCharacter = currentCharacter.copy(isPublic = newIsPublic)

            val result = updateCharacterUseCase(characterId, updatedCharacter)

            result.onSuccess { updatedData ->
                _character.value = updatedData
            }.onFailure { exception ->
                println("Error")
            }
        }
    }

    fun deleteCharacter(characterId: Int) {
        viewModelScope.launch {
            try {
                deleteCharacterUseCase(characterId)
                _characterDeletedSuccessfully.value = true
            } catch (e: Exception) {
                _characterDeletedSuccessfully.value = false
            }
        }
    }

    fun resetCharacterDeletedSuccessfullyState() {
        _characterDeletedSuccessfully.value = false
    }

}
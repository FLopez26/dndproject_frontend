package com.fls.dndproject_frontend.presentation.viewmodel.characterInfo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fls.dndproject_frontend.domain.model.Characters
import com.fls.dndproject_frontend.domain.usecase.characters.CharactersInfoUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CharacterInfoViewModel(
    private val charactersInfoUseCase: CharactersInfoUseCase
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

}
package com.fls.dndproject_frontend.presentation.viewmodel.forum

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fls.dndproject_frontend.domain.model.Characters
import com.fls.dndproject_frontend.domain.usecase.characters.GetAllCharactersUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class ForumViewModel(
    private val getAllCharactersUseCase: GetAllCharactersUseCase
) : ViewModel() {

    private val _publicCharacters = MutableStateFlow<List<Characters>>(emptyList())
    val publicCharacters: StateFlow<List<Characters>> = _publicCharacters

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _hasPublicCharacters = MutableStateFlow<Boolean>(false)
    val hasPublicCharacters: StateFlow<Boolean> = _hasPublicCharacters


    init {
        loadPublicCharacters()
    }

    fun loadPublicCharacters() {
        viewModelScope.launch {
            getAllCharactersUseCase()
                .onStart { _isLoading.value = true }
                .map { allCharacters ->
                    allCharacters.filter { it.isPublic == true }
                }
                .catch { e ->
                    println("Error loading public characters: ${e.message}")
                    _publicCharacters.value = emptyList()
                    _hasPublicCharacters.value = false
                    _isLoading.value = false
                }
                .collectLatest { filteredPublicCharacters ->
                    _publicCharacters.value = filteredPublicCharacters
                    _hasPublicCharacters.value = filteredPublicCharacters.isNotEmpty()
                    _isLoading.value = false
                }
        }
    }
}
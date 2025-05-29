package com.fls.dndproject_frontend.presentation.viewmodel.forum

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fls.dndproject_frontend.domain.model.Characters
import com.fls.dndproject_frontend.domain.usecase.characters.GetAllCharactersUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest // Importa collectLatest
import kotlinx.coroutines.flow.map // Importa map
import kotlinx.coroutines.flow.catch // Importa catch
import kotlinx.coroutines.flow.onStart // Importa onStart
import kotlinx.coroutines.launch

class ForumViewModel(
    private val getAllCharactersUseCase: GetAllCharactersUseCase
) : ViewModel() {

    private val _publicCharacters = MutableStateFlow<List<Characters>>(emptyList())
    val publicCharacters: StateFlow<List<Characters>> = _publicCharacters

    private val _isLoading = MutableStateFlow(true) // True al inicio para indicar carga
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _hasPublicCharacters = MutableStateFlow<Boolean>(false) // False por defecto
    val hasPublicCharacters: StateFlow<Boolean> = _hasPublicCharacters


    init {
        loadPublicCharacters() // Carga los personajes al inicializar el ViewModel
    }

    fun loadPublicCharacters() {
        viewModelScope.launch {
            getAllCharactersUseCase()
                .onStart { _isLoading.value = true } // Emite true al inicio de la carga
                .map { allCharacters -> // Mapea y filtra los personajes
                    allCharacters.filter { it.isPublic == true }
                }
                .catch { e -> // Captura errores
                    println("Error loading public characters: ${e.message}")
                    _publicCharacters.value = emptyList()
                    _hasPublicCharacters.value = false
                    _isLoading.value = false
                }
                .collectLatest { filteredPublicCharacters -> // Recolecta los resultados
                    _publicCharacters.value = filteredPublicCharacters
                    _hasPublicCharacters.value = filteredPublicCharacters.isNotEmpty()
                    _isLoading.value = false // Carga completada
                }
        }
    }
}
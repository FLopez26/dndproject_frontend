package com.fls.dndproject_frontend.presentation.viewmodel.wizard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fls.dndproject_frontend.domain.model.Background
import com.fls.dndproject_frontend.domain.model.CharacterClass
import com.fls.dndproject_frontend.domain.model.Race
import com.fls.dndproject_frontend.domain.usecase.background.GetAllBackgroundsUseCase
import com.fls.dndproject_frontend.domain.usecase.characterClass.GetAllCharacterClassesUseCase
import com.fls.dndproject_frontend.domain.usecase.race.GetAllRacesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class Wizard2ViewModel(
    private val getAllRacesUseCase: GetAllRacesUseCase,
    private val getAllCharacterClassesUseCase: GetAllCharacterClassesUseCase,
    private val getAllBackgroundsUseCase: GetAllBackgroundsUseCase
) : ViewModel() {

    // --- Razas ---
    private val _races = MutableStateFlow<List<Race>>(emptyList())
    val races: StateFlow<List<Race>> = _races.asStateFlow()

    // Raza seleccionada actualmente
    private val _selectedRace = MutableStateFlow<Race?>(null)
    val selectedRace: StateFlow<Race?> = _selectedRace.asStateFlow()

    // --- Clases ---
    private val _classes = MutableStateFlow<List<CharacterClass>>(emptyList())
    val classes: StateFlow<List<CharacterClass>> = _classes.asStateFlow()

    // Clase seleccionada actualmente
    private val _selectedClass = MutableStateFlow<CharacterClass?>(null)
    val selectedClass: StateFlow<CharacterClass?> = _selectedClass.asStateFlow()

    // --- Trasfondos ---
    private val _backgrounds = MutableStateFlow<List<Background>>(emptyList())
    val backgrounds: StateFlow<List<Background>> = _backgrounds.asStateFlow()

    // Trasfondo seleccionado actualmente
    private val _selectedBackground = MutableStateFlow<Background?>(null)
    val selectedBackground: StateFlow<Background?> = _selectedBackground.asStateFlow()

    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {
            getAllRacesUseCase().collectLatest { racesList ->
                _races.value = racesList
                // Opcional: Seleccionar la primera raza por defecto si la lista no está vacía
                if (racesList.isNotEmpty()) {
                    _selectedRace.value = racesList.first()
                }
                println("DEBUG: Razas cargadas (limpio): ${racesList.size} - $racesList")
            }
        }

        viewModelScope.launch {
            getAllCharacterClassesUseCase().collectLatest { classesList ->
                _classes.value = classesList
                // Opcional: Seleccionar la primera clase por defecto
                if (classesList.isNotEmpty()) {
                    _selectedClass.value = classesList.first()
                }
                println("DEBUG: Clases cargadas (limpio): ${classesList.size} - $classesList")
            }
        }

        viewModelScope.launch {
            getAllBackgroundsUseCase().collectLatest { backgroundsList ->
                _backgrounds.value = backgroundsList
                // Opcional: Seleccionar el primer trasfondo por defecto
                if (backgroundsList.isNotEmpty()) {
                    _selectedBackground.value = backgroundsList.first()
                }
                println("DEBUG: Trasfondos cargados (limpio): ${backgroundsList.size} - $backgroundsList")
            }
        }
    }

    /**
     * Establece la raza seleccionada.
     */
    fun selectRace(race: Race) {
        _selectedRace.value = race
        println("DEBUG: Raza seleccionada: ${race.name}")
    }

    /**
     * Establece la clase seleccionada.
     */
    fun selectCharacterClass(charClass: CharacterClass) {
        _selectedClass.value = charClass
        println("DEBUG: Clase seleccionada: ${charClass.name}")
    }

    /**
     * Establece el trasfondo seleccionado.
     */
    fun selectBackground(background: Background) {
        _selectedBackground.value = background
        println("DEBUG: Trasfondo seleccionado: ${background.name}")
    }

    /**
     * Método para validar que se ha seleccionado al menos un elemento de cada tipo.
     */
    fun areSelectionsComplete(): Boolean {
        return _selectedRace.value != null &&
                _selectedClass.value != null &&
                _selectedBackground.value != null
    }

    /**
     * Retorna los elementos seleccionados para la siguiente pantalla.
     */
    fun getSelectedData(): Triple<Race?, CharacterClass?, Background?> {
        return Triple(_selectedRace.value, _selectedClass.value, _selectedBackground.value)
    }
}
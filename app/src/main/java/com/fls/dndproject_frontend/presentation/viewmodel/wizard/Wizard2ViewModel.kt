package com.fls.dndproject_frontend.presentation.viewmodel.wizard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fls.dndproject_frontend.domain.model.Background
import com.fls.dndproject_frontend.domain.model.CharacterClass
import com.fls.dndproject_frontend.domain.model.Race
import com.fls.dndproject_frontend.domain.model.Abilities
import com.fls.dndproject_frontend.domain.model.Equipment
import com.fls.dndproject_frontend.domain.model.Competencies
import com.fls.dndproject_frontend.domain.model.Stats
import com.fls.dndproject_frontend.domain.model.StatsChange
import com.fls.dndproject_frontend.domain.model.Characters
import com.fls.dndproject_frontend.domain.model.User
import com.fls.dndproject_frontend.domain.usecase.background.GetAllBackgroundsUseCase
import com.fls.dndproject_frontend.domain.usecase.characterClass.GetAllCharacterClassesUseCase
import com.fls.dndproject_frontend.domain.usecase.characters.CreateCharacterUseCase
import com.fls.dndproject_frontend.domain.usecase.race.GetAllRacesUseCase
import com.fls.dndproject_frontend.domain.usecase.users.GetUserByIdUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.combine
import kotlin.random.Random
import kotlinx.coroutines.flow.firstOrNull

class Wizard2ViewModel(
    private val getAllRacesUseCase: GetAllRacesUseCase,
    private val getAllCharacterClassesUseCase: GetAllCharacterClassesUseCase,
    private val getAllBackgroundsUseCase: GetAllBackgroundsUseCase,
    private val getUserByIdUseCase: GetUserByIdUseCase,
    private val createCharacterUseCase: CreateCharacterUseCase
) : ViewModel() {

    // ... (Todos tus StateFlows existentes) ...

    private val _characterCreationStatus = MutableStateFlow<Result<Unit>?>(null)
    val characterCreationStatus: StateFlow<Result<Unit>?> = _characterCreationStatus.asStateFlow()

    private val _races = MutableStateFlow<List<Race>>(emptyList())
    val races: StateFlow<List<Race>> = _races.asStateFlow()

    private val _classes = MutableStateFlow<List<CharacterClass>>(emptyList())
    val classes: StateFlow<List<CharacterClass>> = _classes.asStateFlow()

    private val _backgrounds = MutableStateFlow<List<Background>>(emptyList())
    val backgrounds: StateFlow<List<Background>> = _backgrounds.asStateFlow()

    private val _selectedRace = MutableStateFlow<Race?>(null)
    val selectedRace: StateFlow<Race?> = _selectedRace.asStateFlow()

    private val _selectedClass = MutableStateFlow<CharacterClass?>(null)
    val selectedClass: StateFlow<CharacterClass?> = _selectedClass.asStateFlow()

    private val _selectedBackground = MutableStateFlow<Background?>(null)
    val selectedBackground: StateFlow<Background?> = _selectedBackground.asStateFlow()

    private val _characterName = MutableStateFlow("")
    val characterName: StateFlow<String> = _characterName.asStateFlow()

    private val _characterDescription = MutableStateFlow("")
    val characterDescription: StateFlow<String> = _characterDescription.asStateFlow()

    private val _personalityTraits = MutableStateFlow("")
    val personalityTraits: StateFlow<String> = _personalityTraits.asStateFlow()

    private val _ideals = MutableStateFlow("")
    val ideals: StateFlow<String> = _ideals.asStateFlow()

    private val _bonds = MutableStateFlow("")
    val bonds: StateFlow<String> = _bonds.asStateFlow()

    private val _flaws = MutableStateFlow("")
    val flaws: StateFlow<String> = _flaws.asStateFlow()

    private val _generatedAbilities = MutableStateFlow<Abilities?>(null)
    val generatedAbilities: StateFlow<Abilities?> = _generatedAbilities.asStateFlow()

    private val _generatedEquipment = MutableStateFlow<Equipment?>(null)
    val generatedEquipment: StateFlow<Equipment?> = _generatedEquipment.asStateFlow()

    private val _generatedCompetencies = MutableStateFlow<Competencies?>(null)
    val generatedCompetencies: StateFlow<Competencies?> = _generatedCompetencies.asStateFlow()

    private val _generatedStats = MutableStateFlow<Stats?>(null)
    val generatedStats: StateFlow<Stats?> = _generatedStats.asStateFlow()

    private val _generatedImage = MutableStateFlow<ByteArray?>(null)
    val generatedImage: StateFlow<ByteArray?> = _generatedImage.asStateFlow()

    private val _isPublic = MutableStateFlow(false)
    val isPublic: StateFlow<Boolean> = _isPublic.asStateFlow()


    init {
        loadData()
        viewModelScope.launch {
            combine(selectedRace, selectedClass, selectedBackground) { race, charClass, background ->
                Triple(race, charClass, background)
            }.collectLatest { (race, charClass, background) ->
                if (race != null || charClass != null || background != null) {
                    generateHiddenCharacterData(race, charClass, background)
                } else {
                    _generatedAbilities.value = null
                    _generatedEquipment.value = null
                    _generatedCompetencies.value = null
                    _generatedStats.value = null
                }
            }
        }
    }

    fun loadData() {
        viewModelScope.launch {
            getAllRacesUseCase().collectLatest { racesList ->
                _races.value = racesList
                if (racesList.isNotEmpty() && _selectedRace.value == null) {
                    _selectedRace.value = racesList.first()
                }
            }
        }
        viewModelScope.launch {
            getAllCharacterClassesUseCase().collectLatest { classesList ->
                _classes.value = classesList
                if (classesList.isNotEmpty() && _selectedClass.value == null) {
                    _selectedClass.value = classesList.first()
                }
            }
        }
        viewModelScope.launch {
            getAllBackgroundsUseCase().collectLatest { backgroundsList ->
                _backgrounds.value = backgroundsList
                if (backgroundsList.isNotEmpty() && _selectedBackground.value == null) {
                    _selectedBackground.value = backgroundsList.first()
                }
            }
        }
    }

    fun selectRace(race: Race) {
        _selectedRace.value = race
    }

    fun selectCharacterClass(charClass: CharacterClass) {
        _selectedClass.value = charClass
    }

    fun selectBackground(background: Background) {
        _selectedBackground.value = background
    }

    fun setCharacterName(name: String) { _characterName.value = name }
    fun setCharacterDescription(description: String) { _characterDescription.value = description }
    fun setPersonalityTraits(traits: String) { _personalityTraits.value = traits }
    fun setIdeals(ideals: String) { _ideals.value = ideals }
    fun setBonds(bonds: String) { _bonds.value = bonds }
    fun setFlaws(flaws: String) { _flaws.value = flaws }

    /**
     * Genera todos los objetos ocultos (Abilities, Equipment, Competencies, Stats, Image, isPublic)
     * basándose en las selecciones de Raza, Clase y Trasfondo.
     */
    private fun generateHiddenCharacterData(race: Race?, charClass: CharacterClass?, background: Background?) {
        // --- Generar Abilities ---
        val newAbilities = Abilities(
            abilityId = null, // <--- ¡CAMBIADO A NULL!
            race = race,
            characterClass = charClass
        )
        _generatedAbilities.value = newAbilities

        // --- Generar Equipment ---
        val newEquipment = Equipment(
            equipmentId = null, // <--- ¡CAMBIADO A NULL!
            characterClass = charClass,
            background = background
        )
        _generatedEquipment.value = newEquipment

        // --- Generar Competencies ---
        val newCompetencies = Competencies(
            competencyId = null, // <--- ¡CAMBIADO A NULL!
            characterClass = charClass,
            background = background
        )
        _generatedCompetencies.value = newCompetencies

        // --- Generar Stats ---
        val baseStrength = Random.nextInt(8, 13)
        val baseDexterity = Random.nextInt(8, 13)
        val baseConstitution = Random.nextInt(8, 13)
        val baseIntelligence = Random.nextInt(8, 13)
        val baseWisdom = Random.nextInt(8, 13)
        val baseCharisma = Random.nextInt(8, 13)
        val baseHitPoints = Random.nextInt(15, 26)

        val raceStatsChange = race?.statsChange

        val finalStrength = baseStrength + (raceStatsChange?.strength ?: 0)
        val finalDexterity = baseDexterity + (raceStatsChange?.dexterity ?: 0)
        val finalConstitution = baseConstitution + (raceStatsChange?.constitution ?: 0)
        val finalIntelligence = baseIntelligence + (raceStatsChange?.intelligence ?: 0)
        val finalWisdom = baseWisdom + (raceStatsChange?.wisdom ?: 0)
        val finalCharisma = baseCharisma + (raceStatsChange?.charisma ?: 0)
        val finalHitPoints = baseHitPoints + (raceStatsChange?.hitPoints ?: 0)

        val newStats = Stats(
            statsId = null, // <--- ¡CAMBIADO A NULL!
            strength = finalStrength,
            dexterity = finalDexterity,
            constitution = finalConstitution,
            intelligence = finalIntelligence,
            wisdom = finalWisdom,
            charisma = finalCharisma,
            hitPoints = finalHitPoints,
            statsChange = raceStatsChange
        )
        _generatedStats.value = newStats

        _generatedImage.value = null
        _isPublic.value = false
    }

    fun areSelectionsComplete(): Boolean {
        return _selectedRace.value != null &&
                _selectedClass.value != null &&
                _selectedBackground.value != null &&
                _characterName.value.isNotBlank() &&
                _generatedAbilities.value != null &&
                _generatedEquipment.value != null &&
                _generatedCompetencies.value != null &&
                _generatedStats.value != null
    }

    /**
     * Función que la UI llamará para iniciar la creación del personaje.
     * Esta función llamará al UseCase.
     */
    fun createCharacter(userId: Int) {
        viewModelScope.launch {
            _characterCreationStatus.value = null

            if (!areSelectionsComplete()) {
                _characterCreationStatus.value = Result.failure(IllegalStateException("Faltan datos para crear el personaje."))
                return@launch
            }

            println("--- DEBUG: Antes de construir finalCharacter ---")
            println("Character Name: ${characterName.value}")
            println("Description: ${characterDescription.value}")
            println("Personality Traits: ${personalityTraits.value}")
            println("Ideals: ${ideals.value}")
            println("Bonds: ${bonds.value}")
            println("Flaws: ${flaws.value}")

            println("Selected Race: ${selectedRace.value?.name} (ID: ${selectedRace.value?.raceId})")
            println("Selected Class: ${selectedClass.value?.name} (ID: ${selectedClass.value?.classId})")
            println("Selected Background: ${selectedBackground.value?.name} (ID: ${selectedBackground.value?.backgroundId})")

            println("Generated Abilities: ${generatedAbilities.value}")
            println("  Abilities ID: ${generatedAbilities.value?.abilityId}")
            println("  Abilities Race: ${generatedAbilities.value?.race?.name} (ID: ${generatedAbilities.value?.race?.raceId})")
            println("  Abilities Class: ${generatedAbilities.value?.characterClass?.name} (ID: ${generatedAbilities.value?.characterClass?.classId})")

            println("Generated Equipment: ${generatedEquipment.value}")
            println("  Equipment ID: ${generatedEquipment.value?.equipmentId}")
            println("  Equipment Class: ${generatedEquipment.value?.characterClass?.name} (ID: ${generatedEquipment.value?.characterClass?.classId})")
            println("  Equipment Background: ${generatedEquipment.value?.background?.name} (ID: ${generatedEquipment.value?.background?.backgroundId})")

            println("Generated Competencies: ${generatedCompetencies.value}")
            println("  Competencies ID: ${generatedCompetencies.value?.competencyId}")
            println("  Competencies Class: ${generatedCompetencies.value?.characterClass?.name} (ID: ${generatedCompetencies.value?.characterClass?.classId})")
            println("  Competencies Background: ${generatedCompetencies.value?.background?.name} (ID: ${generatedCompetencies.value?.background?.backgroundId})")

            println("Generated Stats: ${generatedStats.value}")
            println("  Stats ID: ${generatedStats.value?.statsId}")
            println("  Strength: ${generatedStats.value?.strength}")
            println("  Dexterity: ${generatedStats.value?.dexterity}")
            // ... añade más stats si lo necesitas

            println("Is Public: ${isPublic.value}")
            println("User ID passed to getFinalCharacterData: $userId")

            val finalCharacter = getFinalCharacterData(userId)

            if (finalCharacter == null) {
                _characterCreationStatus.value = Result.failure(IllegalStateException("No se pudo construir el objeto Characters completo."))
                return@launch
            }

            _characterCreationStatus.value = createCharacterUseCase(finalCharacter)
        }
    }

    /**
     * Prepara y retorna el objeto Characters completo.
     * Es 'private' porque solo lo usará la función 'createCharacter'.
     * Ya es 'suspend' porque usa getUserByIdUseCase().firstOrNull().
     */
    private suspend fun getFinalCharacterData(userId: Int): Characters? {
        val fullUser: User? = getUserByIdUseCase(userId).firstOrNull()

        if (fullUser == null) {
            println("ERROR: No se pudo obtener el objeto User completo para userId: $userId")
            return null
        }

        return Characters(
            characterId = null,
            name = characterName.value,
            description = characterDescription.value.takeIf { it.isNotBlank() },
            personalityTraits = personalityTraits.value.takeIf { it.isNotBlank() },
            ideals = ideals.value.takeIf { it.isNotBlank() },
            bonds = bonds.value.takeIf { it.isNotBlank() },
            flaws = flaws.value.takeIf { it.isNotBlank() },
            stats = generatedStats.value!!,
            characterRace = selectedRace.value!!,
            characterClass = selectedClass.value!!,
            background = selectedBackground.value!!,
            abilities = generatedAbilities.value!!,
            equipment = generatedEquipment.value!!,
            competencies = generatedCompetencies.value!!,
            image = generatedImage.value,
            isPublic = isPublic.value,
            user = fullUser
        )
    }
}
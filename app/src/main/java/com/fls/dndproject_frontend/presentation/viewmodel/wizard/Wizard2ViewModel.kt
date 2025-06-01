package com.fls.dndproject_frontend.presentation.viewmodel.wizard

import android.app.Application
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.fls.dndproject_frontend.domain.model.Abilities
import com.fls.dndproject_frontend.domain.model.Background
import com.fls.dndproject_frontend.domain.model.CharacterClass
import com.fls.dndproject_frontend.domain.model.Characters
import com.fls.dndproject_frontend.domain.model.Competencies
import com.fls.dndproject_frontend.domain.model.Equipment
import com.fls.dndproject_frontend.domain.model.Race
import com.fls.dndproject_frontend.domain.model.Stats
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
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.IOException
import kotlin.random.Random

class Wizard2ViewModel(
    application: Application, // Es necesario tener el contexto de la aplicación para poder mostrar la imagen (URI)
    private val getAllRacesUseCase: GetAllRacesUseCase,
    private val getAllCharacterClassesUseCase: GetAllCharacterClassesUseCase,
    private val getAllBackgroundsUseCase: GetAllBackgroundsUseCase,
    private val getUserByIdUseCase: GetUserByIdUseCase,
    private val createCharacterUseCase: CreateCharacterUseCase
) : AndroidViewModel(application) {

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

    private val _generatedImageBase64 = MutableStateFlow<String?>(null)
    val generatedImageBase64: StateFlow<String?> = _generatedImageBase64.asStateFlow()

    private val _selectedImageUri = MutableStateFlow<Uri?>(null)
    val selectedImageUri: StateFlow<Uri?> = _selectedImageUri.asStateFlow()

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

    fun setSelectedImageUri(uri: Uri?) {
        _selectedImageUri.value = uri
        if (uri != null) {
            viewModelScope.launch {
                val originalBytes = uriToByteArray(uri)
                if (originalBytes != null) {
                    val compressedBytes = compressImage(originalBytes, 70)
                    if (compressedBytes != null) {
                        _generatedImageBase64.value = Base64.encodeToString(compressedBytes, Base64.DEFAULT)
                    } else {
                        _generatedImageBase64.value = null
                    }
                } else {
                    _generatedImageBase64.value = null
                }
            }
        } else {
            _generatedImageBase64.value = null
        }
    }

    private fun uriToByteArray(uri: Uri): ByteArray? {
        val contentResolver = getApplication<Application>().contentResolver
        return try {
            contentResolver.openInputStream(uri)?.use { inputStream ->
                inputStream.readBytes()
            }
        } catch (e: IOException) {
            null
        }
    }

    private fun compressImage(byteArray: ByteArray, quality: Int): ByteArray? {
        return try {
            val bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
            val outputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
            outputStream.toByteArray()
        } catch (e: Exception) {
            null
        }
    }

    private fun generateHiddenCharacterData(race: Race?, charClass: CharacterClass?, background: Background?) {

        val newAbilities = Abilities(
            abilityId = null,
            race = race,
            characterClass = charClass
        )
        _generatedAbilities.value = newAbilities

        val newEquipment = Equipment(
            equipmentId = null,
            characterClass = charClass,
            background = background
        )
        _generatedEquipment.value = newEquipment

        val newCompetencies = Competencies(
            competencyId = null,
            characterClass = charClass,
            background = background
        )
        _generatedCompetencies.value = newCompetencies

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
            statsId = null,
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

    fun createCharacter(userId: Int) {
        viewModelScope.launch {
            _characterCreationStatus.value = null

            if (!areSelectionsComplete()) {
                _characterCreationStatus.value = Result.failure(IllegalStateException("Faltan datos obligatorios para crear el personaje."))
                return@launch
            }

            val finalCharacter = getFinalCharacterData(userId)

            if (finalCharacter == null) {
                _characterCreationStatus.value = Result.failure(IllegalStateException("No se pudo crear el personaje."))
                return@launch
            }

            try {
                _characterCreationStatus.value = createCharacterUseCase(finalCharacter)
            } catch (e: Exception) {
                e.printStackTrace()
                _characterCreationStatus.value = Result.failure(e)
            }
        }
    }

    private suspend fun getFinalCharacterData(userId: Int): Characters? {
        val fullUser: User? = getUserByIdUseCase(userId).firstOrNull()

        if (fullUser == null) {
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
            image = generatedImageBase64.value,
            isPublic = isPublic.value,
            user = fullUser
        )
    }
}
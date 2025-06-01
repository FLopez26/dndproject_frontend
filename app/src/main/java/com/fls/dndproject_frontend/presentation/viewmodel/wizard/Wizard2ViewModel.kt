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
import com.fls.dndproject_frontend.domain.model.Characters // Asegúrate de que el campo 'image' aquí sea String?
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

// Cambia de ViewModel a AndroidViewModel para acceder al contexto de la aplicación
class Wizard2ViewModel(
    application: Application, // Añade application como parámetro del constructor
    private val getAllRacesUseCase: GetAllRacesUseCase,
    private val getAllCharacterClassesUseCase: GetAllCharacterClassesUseCase,
    private val getAllBackgroundsUseCase: GetAllBackgroundsUseCase,
    private val getUserByIdUseCase: GetUserByIdUseCase,
    private val createCharacterUseCase: CreateCharacterUseCase
) : AndroidViewModel(application) { // Llama al constructor de AndroidViewModel con application

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

    // Guarda la imagen como un String Base64 para enviarla a la API
    private val _generatedImageBase64 = MutableStateFlow<String?>(null)
    val generatedImageBase64: StateFlow<String?> = _generatedImageBase64.asStateFlow()

    // Para mostrar la imagen en la UI, guardamos la URI directamente
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

    /**
     * Establece la URI de la imagen seleccionada, la convierte a ByteArray, la comprime
     * y luego la codifica a Base64 para guardarla en el ViewModel.
     */
    fun setSelectedImageUri(uri: Uri?) {
        _selectedImageUri.value = uri // Guarda la URI para la UI
        if (uri != null) {
            viewModelScope.launch {
                val originalBytes = uriToByteArray(uri)
                if (originalBytes != null) {
                    // Comprime la imagen antes de codificarla a Base64
                    val compressedBytes = compressImage(originalBytes, 70) // 70% de calidad
                    if (compressedBytes != null) {
                        _generatedImageBase64.value = Base64.encodeToString(compressedBytes, Base64.DEFAULT)
                        println("--- Imagen convertida a Base64. Longitud: ${_generatedImageBase64.value?.length} ---")
                    } else {
                        _generatedImageBase64.value = null
                        println("--- No se pudo comprimir la imagen ---")
                    }
                } else {
                    _generatedImageBase64.value = null
                    println("--- No se pudo convertir la imagen de URI a bytes originales ---")
                }
            }
        } else {
            _generatedImageBase64.value = null
        }
    }

    /**
     * Convierte una URI de imagen a un ByteArray.
     * Necesita el contexto de la aplicación para acceder al ContentResolver.
     */
    private fun uriToByteArray(uri: Uri): ByteArray? {
        val contentResolver = getApplication<Application>().contentResolver
        return try {
            contentResolver.openInputStream(uri)?.use { inputStream ->
                inputStream.readBytes()
            }
        } catch (e: IOException) {
            println("Error al leer la imagen de la URI: ${e.message}")
            null
        }
    }

    /**
     * Comprime un ByteArray de imagen a un formato JPEG con una calidad dada.
     */
    private fun compressImage(byteArray: ByteArray, quality: Int): ByteArray? {
        return try {
            val bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
            val outputStream = ByteArrayOutputStream()
            // Comprime a JPEG. Puedes cambiar a PNG si lo prefieres, pero JPEG es mejor para fotos.
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
            outputStream.toByteArray()
        } catch (e: Exception) {
            println("Error al comprimir la imagen: ${e.message}")
            null
        }
    }

    /**
     * Genera todos los objetos ocultos (Abilities, Equipment, Competencies, Stats, isPublic)
     * basándose en las selecciones de Raza, Clase y Trasfondo.
     */
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

    // La imagen no es obligatoria para la creación del personaje
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
                _characterCreationStatus.value = Result.failure(IllegalStateException("No se pudo construir el objeto Characters completo. Verificar userId o datos generados."))
                return@launch
            }

            try {
                // Realiza la llamada al UseCase y actualiza el estado
                _characterCreationStatus.value = createCharacterUseCase(finalCharacter)
            } catch (e: Exception) {
                println("Error en Wizard2ViewModel.createCharacter: ${e.message}")
                e.printStackTrace()
                _characterCreationStatus.value = Result.failure(e)
            }
        }
    }

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
            image = generatedImageBase64.value, // ¡CAMBIADO AQUÍ! Ya no necesitas .toByteArray(Charsets.UTF_8)
            isPublic = isPublic.value,
            user = fullUser
        )
    }
}
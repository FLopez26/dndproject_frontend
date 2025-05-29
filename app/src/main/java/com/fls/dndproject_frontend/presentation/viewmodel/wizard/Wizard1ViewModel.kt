package com.fls.dndproject_frontend.presentation.viewmodel.wizard

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class Wizard1ViewModel : ViewModel() {

    private val _characterName = MutableStateFlow("")
    val characterName: StateFlow<String> = _characterName.asStateFlow()

    private val _description = MutableStateFlow("")
    val description: StateFlow<String> = _description.asStateFlow()

    private val _personalityTraits = MutableStateFlow("")
    val personalityTraits: StateFlow<String> = _personalityTraits.asStateFlow()

    private val _ideals = MutableStateFlow("")
    val ideals: StateFlow<String> = _ideals.asStateFlow()

    private val _bonds = MutableStateFlow("")
    val bonds: StateFlow<String> = _bonds.asStateFlow()

    private val _flaws = MutableStateFlow("")
    val flaws: StateFlow<String> = _flaws.asStateFlow()

    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message.asStateFlow()

    fun setCharacterName(name: String) {
        if (name.length <= 16) {
            _characterName.value = name
        } else {
            _message.value = "El nombre no puede exceder los 16 caracteres."
        }
    }

    fun setDescription(desc: String) {
        if (desc.length <= 250) {
            _description.value = desc
        } else {
            _message.value = "La descripción no puede exceder los 250 caracteres."
        }
    }

    fun setPersonalityTraits(traits: String) {
        if (traits.length <= 150) {
            _personalityTraits.value = traits
        } else {
            _message.value = "Los rasgos de personalidad no pueden exceder los 150 caracteres."
        }
    }

    fun setIdeals(idealsText: String) {
        if (idealsText.length <= 150) {
            _ideals.value = idealsText
        } else {
            _message.value = "Los ideales no pueden exceder los 150 caracteres."
        }
    }

    fun setBonds(bondsText: String) {
        if (bondsText.length <= 150) {
            _bonds.value = bondsText
        } else {
            _message.value = "Los vínculos no pueden exceder los 150 caracteres."
        }
    }

    fun setFlaws(flawsText: String) {
        if (flawsText.length <= 150) {
            _flaws.value = flawsText
        } else {
            _message.value = "Los defectos no pueden exceder los 150 caracteres."
        }
    }

    fun validateAndProceed(): Boolean {
        _message.value = null

        if (_characterName.value.isBlank()) {
            _message.value = "El nombre del personaje es obligatorio."
            return false
        }
        if (_description.value.isBlank()) {
            _message.value = "La descripción es obligatoria."
            return false
        }
        if (_personalityTraits.value.isBlank()) {
            _message.value = "Los rasgos de personalidad son obligatorios."
            return false
        }
        if (_ideals.value.isBlank()) {
            _message.value = "Los ideales son obligatorios."
            return false
        }
        if (_bonds.value.isBlank()) {
            _message.value = "Los vínculos son obligatorios."
            return false
        }
        if (_flaws.value.isBlank()) {
            _message.value = "Los defectos son obligatorios."
            return false
        }

        if (_characterName.value.length > 16) {
            _message.value = "El nombre del personaje no puede exceder los 16 caracteres."
            return false
        }
        if (_description.value.length > 250) {
            _message.value = "La descripción no puede exceder los 250 caracteres."
            return false
        }
        if (_personalityTraits.value.length > 150) {
            _message.value = "Los rasgos de personalidad no pueden exceder los 150 caracteres."
            return false
        }
        if (_ideals.value.length > 150) {
            _message.value = "Los ideales no pueden exceder los 150 caracteres."
            return false
        }
        if (_bonds.value.length > 150) {
            _message.value = "Los vínculos no pueden exceder los 150 caracteres."
            return false
        }
        if (_flaws.value.length > 150) {
            _message.value = "Los defectos no pueden exceder los 150 caracteres."
            return false
        }

        return true
    }

    fun messageShown() {
        _message.value = null
    }
}
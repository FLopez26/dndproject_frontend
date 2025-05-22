package com.fls.dndproject_frontend.data.model.characters

import com.fls.dndproject_frontend.domain.model.Characters

data class CreateCharactersDto(
    val name: String,
    val description: String?,

    val personalityTraits: String?,
    val ideals: String?,
    val bonds: String?,
    val flaws: String?,

    val statsId: Int?,
    val raceId: Int?,
    val classId: Int?,
    val backgroundId: Int?,

    val abilitiesId: Int?,
    val equipmentId: Int?,
    val competenciesId: Int?,

    val image: ByteArray?,
    val isPublic: Boolean?,
    val userId: Int
) {
    companion object {
        fun fromCharacters(character: Characters) =
            CreateCharactersDto(
                name = character.name,
                description = character.description,

                personalityTraits = character.personalityTraits,
                ideals = character.ideals,
                bonds = character.bonds,
                flaws = character.flaws,

                statsId = character.stats?.statsId,
                raceId = character.characterRace?.raceId,
                classId = character.characterClass?.classId,
                backgroundId = character.background?.backgroundId,

                abilitiesId = character.abilities?.abilityId,
                equipmentId = character.equipment?.equipmentId,
                competenciesId = character.competencies?.competencyId,

                image = character.image,
                isPublic = character.isPublic,
                userId = character.user?.userId ?: 0
            )
    }

    fun toCharacters(id: Int) =
        Characters(
            characterId = id,
            name = name,
            description = description,

            personalityTraits = personalityTraits,
            ideals = ideals,
            bonds = bonds,
            flaws = flaws,

            stats = null,
            characterRace = null,
            characterClass = null,
            background = null,

            abilities = null,
            equipment = null,
            competencies = null,

            image = image,
            isPublic = isPublic,
            user = null
        )
}
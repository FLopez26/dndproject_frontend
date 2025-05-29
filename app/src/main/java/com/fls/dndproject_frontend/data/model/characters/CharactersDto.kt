package com.fls.dndproject_frontend.data.model.characters

import com.fls.dndproject_frontend.data.model.abilities.AbilitiesDto
import com.fls.dndproject_frontend.domain.model.Characters
import com.fls.dndproject_frontend.data.model.background.BackgroundDto
import com.fls.dndproject_frontend.data.model.characterClass.CharacterClassDto
import com.fls.dndproject_frontend.data.model.competencies.CompetenciesDto
import com.fls.dndproject_frontend.data.model.equipment.EquipmentDto
import com.fls.dndproject_frontend.data.model.race.RaceDto
import com.fls.dndproject_frontend.data.model.stats.StatsDto
import com.fls.dndproject_frontend.data.model.user.UserDto

data class CharactersDto(
    val characterId: Int,
    val name: String,
    val description: String?,
    val personalityTraits: String?,
    val ideals: String?,
    val bonds: String?,
    val flaws: String?,
    val stats: StatsDto?,
    val characterRace: RaceDto?,
    val characterClass: CharacterClassDto?,
    val background: BackgroundDto?,
    val abilities: AbilitiesDto?,
    val equipment: EquipmentDto?,
    val competencies: CompetenciesDto?,
    val image: ByteArray?,
    val isPublic: Boolean?,
    val user: UserDto?
) {
    fun toCharacters() =
        Characters(
            characterId = characterId,
            name = name,
            description = description,
            personalityTraits = personalityTraits,
            ideals = ideals,
            bonds = bonds,
            flaws = flaws,
            stats = stats?.toStats(),
            characterRace = characterRace?.toRace(),
            characterClass = characterClass?.toCharacterClass(),
            background = background?.toBackground(),
            abilities = abilities?.toAbilities(),
            equipment = equipment?.toEquipment(),
            competencies = competencies?.toCompetencies(),
            image = image,
            isPublic = isPublic,
            user = user?.toUser()
        )

    companion object {
        fun fromCharacters(character: Characters): CharactersDto {
            return CharactersDto(
                characterId = character.characterId ?: 0,
                name = character.name,
                description = character.description,
                personalityTraits = character.personalityTraits,
                ideals = character.ideals,
                bonds = character.bonds,
                flaws = character.flaws,
                stats = character.stats?.let { StatsDto.fromStats(it) },
                characterRace = character.characterRace?.let { RaceDto.fromRace(it) },
                characterClass = character.characterClass?.let { CharacterClassDto.fromCharacterClass(it) },
                background = character.background?.let { BackgroundDto.fromBackground(it) },
                abilities = character.abilities?.let { AbilitiesDto.fromAbilities(it) },
                equipment = character.equipment?.let { EquipmentDto.fromEquipment(it) },
                competencies = character.competencies?.let { CompetenciesDto.fromCompetencies(it) },
                image = character.image,
                isPublic = character.isPublic,
                user = character.user?.let { UserDto.fromUser(it) }
            )
        }
    }
}
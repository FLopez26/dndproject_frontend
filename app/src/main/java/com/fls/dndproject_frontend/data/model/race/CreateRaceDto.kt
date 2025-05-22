package com.fls.dndproject_frontend.data.model.race

import com.fls.dndproject_frontend.domain.model.Race
import com.fls.dndproject_frontend.data.model.statsChange.CreateStatsChangeDto

data class CreateRaceDto(
    val name: String,
    val selection: Int?,
    val abilitiesText: String?,
    val competenciesText: String?,
    val statsChange: CreateStatsChangeDto?
) {
    companion object {
        fun fromRace(race: Race) =
            CreateRaceDto(
                name = race.name,
                selection = race.selection,
                abilitiesText = race.abilitiesText,
                competenciesText = race.competenciesText,
                statsChange = race.statsChange?.let { CreateStatsChangeDto.fromStatsChange(it) }
            )
    }

    fun toRace(id: Int) =
        Race(
            raceId = id,
            name = name,
            selection = selection,
            abilitiesText = abilitiesText,
            competenciesText = competenciesText,
            statsChange = statsChange?.toStatsChange(0)
        )
}
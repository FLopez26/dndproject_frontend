package com.fls.dndproject_frontend.data.model.race

import com.fls.dndproject_frontend.domain.model.Race
import com.fls.dndproject_frontend.data.model.statsChange.StatsChangeDto

data class RaceDto(
    val raceId: Int,
    val name: String,
    val selection: Int?,
    val abilitiesText: String?,
    val competenciesText: String?,
    val statsChange: StatsChangeDto?
) {
    fun toRace() =
        Race(
            raceId = raceId,
            name = name,
            selection = selection,
            abilitiesText = abilitiesText,
            competenciesText = competenciesText,
            statsChange = statsChange?.toStatsChange()
        )
}
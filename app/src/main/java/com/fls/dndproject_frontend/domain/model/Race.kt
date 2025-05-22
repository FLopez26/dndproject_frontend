package com.fls.dndproject_frontend.domain.model

data class Race(
    val raceId: Int = 0,
    val name: String,
    val selection: Int?,
    val abilitiesText: String?,
    val competenciesText: String?,
    val statsChange: StatsChange? = null
)
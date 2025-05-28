package com.fls.dndproject_frontend.domain.model

data class Race(
    val raceId: Int = 0,
    val name: String,
    val raceSelection: Int?,
    val speed: Int?,
    val abilities: String?,
    val competencies: String?,
    val statsChange: StatsChange? = null
)
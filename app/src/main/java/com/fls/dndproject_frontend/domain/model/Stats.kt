package com.fls.dndproject_frontend.domain.model

data class Stats(
    val statsId: Int?,
    val strength: Int = 0,
    val dexterity: Int = 0,
    val constitution: Int = 0,
    val intelligence: Int = 0,
    val wisdom: Int = 0,
    val charisma: Int = 0,
    val hitPoints: Int = 0,
    val statsChange: StatsChange? = null
)
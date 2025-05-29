package com.fls.dndproject_frontend.domain.usecase.race

import com.fls.dndproject_frontend.data.repository.RaceRestRepository
import com.fls.dndproject_frontend.domain.model.Race
import kotlinx.coroutines.flow.Flow

class GetAllRacesUseCase(
    private val raceRestRepository: RaceRestRepository
) {
    operator fun invoke(): Flow<List<Race>> {
        return raceRestRepository.getAllRaces()
    }
}
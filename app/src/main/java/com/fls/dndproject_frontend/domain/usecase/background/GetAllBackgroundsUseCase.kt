package com.fls.dndproject_frontend.domain.usecase.background

import com.fls.dndproject_frontend.data.repository.BackgroundRestRepository
import com.fls.dndproject_frontend.domain.model.Background
import kotlinx.coroutines.flow.Flow

class GetAllBackgroundsUseCase(
    private val backgroundRepository: BackgroundRestRepository
) {
    operator fun invoke(): Flow<List<Background>> {
        return backgroundRepository.getAllBackgrounds()
    }
}
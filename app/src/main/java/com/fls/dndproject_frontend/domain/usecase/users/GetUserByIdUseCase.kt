package com.fls.dndproject_frontend.domain.usecase.users

import com.fls.dndproject_frontend.data.repository.UserRestRepository
import com.fls.dndproject_frontend.domain.model.User
import kotlinx.coroutines.flow.Flow

class GetUserByIdUseCase(private val userRestRepository: UserRestRepository) {
    operator fun invoke(userId: Int): Flow<User?> {
        return userRestRepository.getUserById(userId)
    }
}
package com.fls.dndproject_frontend.domain.usecase.users

import com.fls.dndproject_frontend.data.repository.UserRestRepository
import com.fls.dndproject_frontend.domain.model.User
import kotlinx.coroutines.flow.Flow

class ListUsersUseCase(
    val userRepository: UserRestRepository
) {
    operator fun invoke(): Flow<List<User>> {
        return userRepository.getAllUsers()
    }
}
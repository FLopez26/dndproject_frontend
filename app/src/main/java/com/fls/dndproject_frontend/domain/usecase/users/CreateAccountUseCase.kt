package com.fls.dndproject_frontend.domain.usecase.users

import com.fls.dndproject_frontend.data.repository.UserRestRepository
import com.fls.dndproject_frontend.domain.model.User

class CreateAccountUseCase(
    val userRepository: UserRestRepository
) {
    suspend operator fun invoke(user: User): User{
        return userRepository.save(user)
    }
}
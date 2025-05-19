package com.fls.dndproject_frontend.domain.usecase.createAccount

import com.fls.dndproject_frontend.data.repository.UserRestRepository
import com.fls.dndproject_frontend.domain.model.User

class CreateAccountUseCase(
    val userRepository: UserRestRepository
) {
    suspend operator fun invoke(user: User){
        userRepository.save(user)
    }
}
package com.fls.dndproject_frontend.data.model.user

import com.fls.dndproject_frontend.domain.model.User

data class CreateUserDto(
    val username: String,
    val email: String,
    val password: String
) {
    companion object {
        fun fromUser(user: User) =
            CreateUserDto(
                username = user.username,
                email = user.email,
                password = user.password
            )
    }

    fun toUser(id: Int) =
        User(
            userId = id,
            username = username,
            email = email,
            password = password
        )
}
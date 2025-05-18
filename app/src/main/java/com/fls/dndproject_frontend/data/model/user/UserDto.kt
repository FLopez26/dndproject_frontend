package com.fls.dndproject_frontend.data.model.user

import com.fls.dndproject_frontend.domain.model.User

data class UserDto(
    val id: Int,
    val username: String,
    val email: String,
    val password: String
) {
    fun toUser() =
        User(
            id = id,
            username = username,
            email = email,
            password = password
        )
}
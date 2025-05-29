package com.fls.dndproject_frontend.data.model.user

import com.fls.dndproject_frontend.domain.model.User

data class UserDto(
    val userId: Int,
    val username: String,
    val email: String,
    val password: String
) {
    fun toUser() =
        User(
            userId = userId,
            username = username,
            email = email,
            password = password
        )

    companion object {
        fun fromUser(user: User): UserDto {
            return UserDto(
                userId = user.userId ?: 0,
                username = user.username,
                email = user.email,
                password = user.password
            )
        }
    }
}
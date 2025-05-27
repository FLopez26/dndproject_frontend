package com.fls.dndproject_frontend.domain.model

data class User(
    val userId: Int?,
    val username: String,
    val email: String,
    val password: String
)
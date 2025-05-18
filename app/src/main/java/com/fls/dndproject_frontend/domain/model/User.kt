package com.fls.dndproject_frontend.domain.model

data class User(
    val id: Int,
    val username: String,
    val email: String,
    val password: String
)
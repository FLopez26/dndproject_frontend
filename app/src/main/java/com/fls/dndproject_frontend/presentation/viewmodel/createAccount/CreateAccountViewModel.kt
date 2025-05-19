package com.fls.dndproject_frontend.presentation.viewmodel.createAccount

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fls.dndproject_frontend.domain.model.User
import com.fls.dndproject_frontend.domain.usecase.createAccount.CreateAccountUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CreateAccountViewModel(
    val createAccountUseCase: CreateAccountUseCase
): ViewModel() {

    private val _username = MutableStateFlow("")
    val username: StateFlow<String> = _username

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password

    fun setUsername(username: String) {
        this._username.value = username
    }

    fun setEmail(email: String) {
        this._email.value = email
    }

    fun setPassword(password: String) {
        this._password.value = password
    }

    fun saveUser(){
        viewModelScope.launch {
            createAccountUseCase(User(0, _username.value, _email.value, _password.value))
        }
    }
}
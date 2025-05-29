package com.fls.dndproject_frontend.presentation.viewmodel.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fls.dndproject_frontend.domain.model.User
import com.fls.dndproject_frontend.domain.usecase.users.GetUserByIdUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val getUserByIdUseCase: GetUserByIdUseCase
) : ViewModel() {

    private val _userProfile = MutableStateFlow<User?>(null)
    val userProfile: StateFlow<User?> = _userProfile.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun loadUserProfile(userId: Int?) {
        if (userId == null) {
            _error.value = "User ID cannot be null."
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                getUserByIdUseCase(userId).collect { user ->
                    _userProfile.value = user
                }
            } catch (e: Exception) {
                _error.value = "Error loading user profile: ${e.localizedMessage ?: "Unknown error"}"
                _userProfile.value = null
            } finally {
                _isLoading.value = false
            }
        }
    }
}
package com.example.domain

import com.example.domain.models.User
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class UserStateUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    val userState: StateFlow<User?> = repository.userState
}

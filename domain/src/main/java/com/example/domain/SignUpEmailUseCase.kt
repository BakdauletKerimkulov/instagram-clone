package com.example.domain

import com.example.domain.models.User
import javax.inject.Inject

class SignUpEmailUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String) : Result<User?> {
        return repository.signUpWithEmail(email, password)
    }
}
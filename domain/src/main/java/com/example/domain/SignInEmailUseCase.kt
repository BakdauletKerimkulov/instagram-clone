package com.example.domain

import com.example.domain.models.User
import javax.inject.Inject

class SignInEmailUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String): Result<User?> {
        return authRepository.signInWithEmail(email, password)
    }
}
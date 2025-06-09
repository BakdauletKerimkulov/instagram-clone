package com.example.domain

import com.example.domain.models.User
import javax.inject.Inject

class CreateUserUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(user: User) : Result<Unit> {
        return userRepository.sendUser(user)
    }

}
package com.example.domain

import javax.inject.Inject

class UpdateDisplayNameUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(displayName: String) = repository.updateDisplayName(displayName)
}
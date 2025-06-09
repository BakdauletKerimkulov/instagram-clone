package com.example.domain

import com.example.domain.models.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface AuthRepository {
    suspend fun signUpWithEmail(email: String, password: String): Result<User?>
    suspend fun signInWithEmail(email: String, password: String): Result<User?>

    fun isUserLoggedIn() : Boolean
    fun getCurrentUser() : User?
    fun signOut()

    suspend fun updateDisplayName(newDisplayName: String) : Result<Unit>

    val userState: StateFlow<User?>
}
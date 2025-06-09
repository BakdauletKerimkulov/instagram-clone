package com.example.data.data_network.repository

import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface RemoteAuthRepository {
    suspend fun signUpWithEmail(email: String, password: String) : Result<FirebaseUser?>
    suspend fun signInWithEmail(email: String, password: String) : Result<FirebaseUser?>

    fun isUserLoggedIn() : Boolean
    fun getCurrentUser() : FirebaseUser?
    fun signOut()

    suspend fun updateDisplayName(displayName: String) : Result<Unit>

    val userState: StateFlow<FirebaseUser?>
}
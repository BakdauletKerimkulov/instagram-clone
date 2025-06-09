package com.example.data.data_network.repository

import com.example.domain.models.User

interface RemoteUserRepository {
    suspend fun createUser(user: User) : Result<Unit>

    suspend fun updateUserField(userUid: String, updates: Map<String, Any?>)

    suspend fun updateUser(user: User)
}
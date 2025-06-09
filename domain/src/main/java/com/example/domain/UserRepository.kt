package com.example.domain

import com.example.domain.models.User

interface UserRepository {
    suspend fun sendUser(user: User) : Result<Unit>
}
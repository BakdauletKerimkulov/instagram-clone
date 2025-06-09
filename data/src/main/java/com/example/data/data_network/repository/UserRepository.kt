package com.example.data.data_network.repository

import com.google.firebase.firestore.core.UserData

interface UserRepository {
    suspend fun createUser()
}
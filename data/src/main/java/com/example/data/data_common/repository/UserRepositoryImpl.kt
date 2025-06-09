package com.example.data.data_common.repository

import com.example.data.data_network.repository.RemoteUserRepository
import com.example.domain.UserRepository
import com.example.domain.models.User
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val repository: RemoteUserRepository
) : UserRepository {
    override suspend fun sendUser(user: User) : Result<Unit> {
        return repository.createUser(user)
    }

}
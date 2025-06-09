package com.example.data.data_common.repository

import com.example.data.data_common.mappers.toDomainResult
import com.example.data.data_common.mappers.toDomainUser
import com.example.data.data_network.repository.RemoteAuthRepository
import com.example.domain.AuthRepository
import com.example.domain.models.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val repository: RemoteAuthRepository,
) : AuthRepository {

    override suspend fun signUpWithEmail(
        email: String,
        password: String,
    ): Result<User?> {
        return repository.signUpWithEmail(email, password).toDomainResult()
    }

    override suspend fun signInWithEmail(
        email: String,
        password: String,
    ): Result<User?> {
        return repository.signInWithEmail(email, password).toDomainResult()
    }

    override fun isUserLoggedIn(): Boolean {
        return repository.isUserLoggedIn()
    }

    override fun getCurrentUser(): User? {
        return repository.getCurrentUser()?.toDomainUser()
    }

    override fun signOut() {
        repository.signOut()
    }

    override suspend fun updateDisplayName(newDisplayName: String): Result<Unit> {
        return repository.updateDisplayName(newDisplayName)
    }

    override val userState: StateFlow<User?> = repository.userState.map { it?.toDomainUser() }.stateIn(
        scope = CoroutineScope(Dispatchers.Default),
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = repository.getCurrentUser()?.toDomainUser(),
    )
}
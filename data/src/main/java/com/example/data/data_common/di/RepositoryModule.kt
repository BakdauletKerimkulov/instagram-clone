package com.example.data.data_common.di

import com.example.data.data_common.repository.AuthRepositoryImpl
import com.example.data.data_common.repository.PreferencesRepository
import com.example.data.data_common.repository.PreferencesRepositoryImpl
import com.example.data.data_common.repository.UserRepositoryImpl
import com.example.domain.AuthRepository
import com.example.domain.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindAuthRepository(impl: AuthRepositoryImpl) : AuthRepository

    @Binds
    abstract fun bindPreferencesRepository(impl: PreferencesRepositoryImpl) : PreferencesRepository

    @Binds
    abstract fun bindUserRepository(impl: UserRepositoryImpl) : UserRepository
}
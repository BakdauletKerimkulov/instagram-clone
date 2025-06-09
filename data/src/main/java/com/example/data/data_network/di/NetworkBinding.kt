package com.example.data.data_network.di

import com.example.data.data_network.repository.RemoteAuthRepository
import com.example.data.data_network.repository.RemoteAuthRepositoryImpl
import com.example.data.data_network.repository.RemoteUserRepository
import com.example.data.data_network.repository.RemoteUserRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class NetworkBinding {
    @Binds
    abstract fun bindRemoteAuthRepository(impl: RemoteAuthRepositoryImpl) : RemoteAuthRepository

    @Binds
    abstract fun bindRemoteUserRepository(impl: RemoteUserRepositoryImpl) : RemoteUserRepository
}
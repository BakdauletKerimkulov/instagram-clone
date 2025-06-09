package com.example.data.data_network.di

import com.example.data.BuildConfig
import com.example.data.data_common.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth() : FirebaseAuth {
        val auth = FirebaseAuth.getInstance()
        if (BuildConfig.DEBUG) {
            auth.useEmulator(Constants.EMULATOR_HOST, Constants.AUTH_EMULATOR_PORT)
        }
        return auth
    }

    @Provides
    @Singleton
    fun provideFirebaseFirestore() : FirebaseFirestore {
        val firestore = FirebaseFirestore.getInstance()
        if (BuildConfig.DEBUG) {
            firestore.useEmulator(Constants.EMULATOR_HOST, Constants.FIRESTORE_EMULATOR_PORT)
        }
        return firestore
    }
}
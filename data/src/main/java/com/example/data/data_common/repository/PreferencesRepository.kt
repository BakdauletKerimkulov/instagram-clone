package com.example.data.data_common.repository

interface PreferencesRepository {
    fun isUserLoggedIn(): Boolean

    fun setUserLoggedIn()
}
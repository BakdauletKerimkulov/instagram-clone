package com.example.data.data_common.repository

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferencesRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : PreferencesRepository {
    private val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    override fun isUserLoggedIn(): Boolean {
        return sharedPreferences.getBoolean(KEY_USER_LOGGED_IN, false)
    }

    override fun setUserLoggedIn() {
        with(sharedPreferences.edit()) {
            putBoolean(KEY_USER_LOGGED_IN, true)
            apply()
        }
    }

    companion object {
        private const val PREF_NAME = "user_prefs"
        private const val KEY_USER_LOGGED_IN = "user_logged_in"
    }
}
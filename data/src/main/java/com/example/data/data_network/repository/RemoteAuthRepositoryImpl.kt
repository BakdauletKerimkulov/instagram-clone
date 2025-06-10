package com.example.data.data_network.repository

import android.util.Log
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthEmailException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthMissingActivityForRecaptchaException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withTimeout
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.Result

@Singleton
class RemoteAuthRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth
) : RemoteAuthRepository {
    override suspend fun signUpWithEmail(
        email: String,
        password: String,
    ): Result<FirebaseUser?> {
        return try {
            withTimeout(TIMEOUT) {
                val result = auth.createUserWithEmailAndPassword(email, password).await()

                Result.success(result.user)
            }
        } catch (e: Exception) {
            Result.failure(handleAuthException(e))
        }
    }

    override suspend fun signInWithEmail(
        email: String,
        password: String,
    ): Result<FirebaseUser?> {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            Result.success(result.user)
        } catch (e: Exception) {
            Result.failure(handleAuthException(e))
        }
    }

    override fun isUserLoggedIn(): Boolean {
        Log.d(TAG, "isUserLoggedIn called")
        return auth.currentUser != null
    }

    override val userState: Flow<FirebaseUser?> = callbackFlow {
        val listener = FirebaseAuth.AuthStateListener {
            val currentUser = it.currentUser

            if (currentUser != null) {
                currentUser.reload().addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val reloadedUser = auth.currentUser
                        if (reloadedUser != null) {
                            trySend(reloadedUser)
                        } else {
                            trySend(null)
                        }
                    } else {
                        Log.e(TAG, "Reload failed: ${task.exception}")
                        trySend(null)
                    }
                }
            } else {
                trySend(null)
            }
        }

        auth.addAuthStateListener(listener)

        awaitClose {
            auth.removeAuthStateListener(listener)
        }
    }.distinctUntilChanged()

    override fun getCurrentUser(): FirebaseUser? {
        Log.d(TAG, "getCurrentUser called. User is ${auth.currentUser?.email ?: "No user"}")
        return auth.currentUser
    }

    override fun signOut() {
        auth.signOut()
    }

    override suspend fun updateDisplayName(newDisplayName: String): Result<Unit> {
        return try {
            val request = UserProfileChangeRequest.Builder()
                .setDisplayName(newDisplayName)
                .build()

            FirebaseAuth.getInstance().currentUser
                ?.updateProfile(request)
                ?.await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(handleAuthException(e))
        }
    }

    private fun handleAuthException(e: Exception): Exception {
        val message = when (e) {
            is FirebaseAuthWeakPasswordException -> "Weak password"
            is FirebaseAuthInvalidCredentialsException -> when (e.errorCode) {
                "ERROR_INVALID_EMAIL" -> "Invalid email"
                "ERROR_WRONG_PASSWORD" -> "Invalid password"
                else -> "Invalid credentials"
            }
            is FirebaseAuthEmailException -> "Invalid email"
            is FirebaseAuthUserCollisionException -> "User already exists"
            is FirebaseAuthInvalidUserException -> "User does not exist"
            is FirebaseTooManyRequestsException -> "Too many requests to log in"
            is FirebaseAuthMissingActivityForRecaptchaException -> "Missing activity"
            is TimeoutCancellationException -> "Timeout exceeded"
            else -> {
                Log.e(TAG, "Authentication failed: $e")
                "Authentication failed: ${e.message}"
            }
        }
        return Exception(message)
    }

    companion object {
        private const val TIMEOUT = 5000L
        private const val TAG = "RemoteAuthRepository"
    }
}
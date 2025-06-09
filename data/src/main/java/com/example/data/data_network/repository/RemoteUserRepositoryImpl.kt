package com.example.data.data_network.repository

import android.util.Log
import com.example.data.data_network.FirebaseConstants
import com.example.domain.models.User
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class RemoteUserRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : RemoteUserRepository {
    override suspend fun createUser(user: User) : Result<Unit> {
        return try {
            firestore
                .collection(FirebaseConstants.USERS)
                .document(user.uid)
                .set(user)
                .await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateUserField(userUid: String, updates: Map<String, Any?>) {
        firestore.collection(FirebaseConstants.USERS)
            .document(userUid)
            .update(updates)
            .addOnSuccessListener { Log.d(TAG, "User updated successfully") }
            .addOnFailureListener { e -> Log.e(TAG, "Error updating user", e) }
    }

    override suspend fun updateUser(user: User) {
        firestore.collection(FirebaseConstants.USERS)
            .document(user.uid)
            .set(user, SetOptions.merge())
            .addOnSuccessListener { Log.d(TAG, "User updated successfully") }
            .addOnFailureListener { e -> Log.e(TAG, "Error updating user", e) }
    }

    companion object {
        private const val TAG = "RemoteUserRepository"
    }
}
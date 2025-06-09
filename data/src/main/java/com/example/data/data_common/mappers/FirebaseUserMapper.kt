package com.example.data.data_common.mappers

import com.example.domain.models.User
import com.google.firebase.auth.FirebaseUser

fun FirebaseUser.toDomainUser(): User {
    return User(
        uid = uid,
        email = email,
        username = displayName?.split(" ")?.first() ?: "Unknown",
        avatarUrl = photoUrl.toString()
    )
}

fun Result<FirebaseUser?>.toDomainResult(): Result<User?> {
    return map { it?.toDomainUser() }
}
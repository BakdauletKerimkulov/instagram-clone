package com.example.domain.models

data class User(
    val uid: String,
    val email: String?,
    val username: String,
    val fullName: String? = null,

    val phoneNumber: String? = null,
    val avatarUrl: String? = null,
    val bio: String? = null,

    val createdAt: Long? = null,
    val birthday: Long? = null,

    val followers: List<String> = emptyList(),
    val following: List<String> = emptyList(),
    val postsCount: Int = 0,
)
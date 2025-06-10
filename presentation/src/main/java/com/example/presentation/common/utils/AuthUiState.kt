package com.example.presentation.common.utils

sealed class AuthUiState {
    object Loading : AuthUiState()
    object Authenticated : AuthUiState()
    object Unauthenticated : AuthUiState()
}
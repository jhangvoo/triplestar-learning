package com.triplestar.learn.data.state

sealed interface AuthState {
    data object Loading : AuthState
    data object Unauthenticated : AuthState
    data class Authenticated(val userId: String, val email: String, val displayName: String) : AuthState
    data class BackendConfigPending(val message: String) : AuthState
    data class Error(val message: String) : AuthState
}

sealed interface UiState<out T> {
    data object Loading : UiState<Nothing>
    data class Success<out T>(val data: T) : UiState<T>
    data class Error(val message: String) : UiState<Nothing>
}

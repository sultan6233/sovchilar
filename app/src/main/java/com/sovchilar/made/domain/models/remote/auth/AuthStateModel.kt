package com.sovchilar.made.domain.models.remote.auth

data class AuthStateModel(val state: AuthState, val token: String? = null, val message: String? = null)
enum class AuthState {
    AUTHENTICATED, UNAUTHENTICATED, INVALID_AUTHENTICATION, LOADED
}
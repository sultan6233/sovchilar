package sovchilar.uz.domain.models.remote.auth

import sovchilar.uz.domain.utils.DataState

sealed class AuthState {
    object Loading : AuthState()
    data class AUTHENTICATED(val authData: AuthResponseModel) : AuthState()
    object UNAUTHENTICATED : AuthState()
    data class INVALID_AUTHENTICATION(val error: String) : AuthState()
    data class CONNECTION_ERROR(val error: String) : AuthState()
}
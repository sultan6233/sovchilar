package com.sovchilar.made
sealed class UserError {
    object RemoteError : UserError()
    // Add more error types if needed
}
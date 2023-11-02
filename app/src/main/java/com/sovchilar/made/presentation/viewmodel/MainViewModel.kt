package com.sovchilar.made.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.sovchilar.made.data.local.usecases.EncryptedSharedPrefsUseCase
import com.sovchilar.made.data.remote.ApiService
import com.sovchilar.made.domain.models.remote.auth.AuthErrorModel
import com.sovchilar.made.domain.models.remote.auth.AuthModel
import com.sovchilar.made.domain.models.remote.auth.AuthResponseModel
import com.sovchilar.made.domain.models.remote.auth.AuthState
import com.sovchilar.made.domain.models.remote.auth.AuthStateModel
import com.sovchilar.made.uitls.authenticated
import com.sovchilar.made.uitls.login
import com.sovchilar.made.uitls.password
import com.sovchilar.made.uitls.token
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {
    val dataReady = MutableLiveData<Boolean>()
    var loginLiveData = MutableLiveData<AuthStateModel>()
    fun loginOrRegisterRequest(login: String, password: String) {
        ApiService.create().loginOrRegister(AuthModel(login, password))
            .enqueue(object : Callback<AuthResponseModel> {
                override fun onResponse(
                    call: Call<AuthResponseModel>, response: Response<AuthResponseModel>
                ) {

                    if (response.isSuccessful) {
                        response.body()?.let {
                            loginLiveData.postValue(
                                AuthStateModel(
                                    AuthState.AUTHENTICATED,
                                    it.token
                                )
                            )
                        } ?: loginLiveData.postValue(
                            AuthStateModel(
                                AuthState.INVALID_AUTHENTICATION,
                                null
                            )
                        )
                    } else {
                        if (response.code() == 400) {
                            val gson = Gson()
                            val errorResponse = gson.fromJson(
                                response.errorBody()?.charStream(),
                                AuthErrorModel::class.java
                            )
                            loginLiveData.postValue(
                                AuthStateModel(
                                    AuthState.INVALID_AUTHENTICATION,
                                    null,
                                    errorResponse.message
                                )
                            )
                        } else {
                            loginLiveData.postValue(
                                AuthStateModel(
                                    AuthState.UNAUTHENTICATED,
                                    null,
                                    "Error code: ${response.code()}"
                                )
                            )
                        }
                    }
                }

                override fun onFailure(call: Call<AuthResponseModel>, t: Throwable) {
                    loginLiveData.postValue(
                        AuthStateModel(
                            AuthState.CONNECTION_ERROR,
                            null,
                            t.message
                        )
                    )
                }
            })
    }

    suspend fun saveCredentials(
        encryptedSharedPrefsUseCase: EncryptedSharedPrefsUseCase,
        loginText: String,
        passwordText: String,
        tokenText: String
    ) {
        encryptedSharedPrefsUseCase.writeIntoFile(
            login, loginText
        )
        encryptedSharedPrefsUseCase.writeIntoFile(
            password, passwordText
        )
        encryptedSharedPrefsUseCase.writeIntoFile(token, tokenText)
        encryptedSharedPrefsUseCase.saveAuthState(authenticated)
    }

}
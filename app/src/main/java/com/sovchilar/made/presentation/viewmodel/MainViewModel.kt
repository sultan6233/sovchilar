package com.sovchilar.made.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sovchilar.made.EncryptedSharedPrefsUseCase

class MainViewModel : ViewModel() {
    val dataReady = MutableLiveData<Boolean>()
    var loginLiveData = MutableLiveData<sovchilar.uz.domain.models.remote.auth.AuthStateModel>()
    fun loginOrRegisterRequest(login: String, password: String) {
//        sovchilar.arch.featureremoteapi.ApiService.create().loginOrRegister(
//            sovchilar.uz.domain.models.remote.auth.AuthModel(
//                login,
//                password
//            )
//        )
//            .enqueue(object : Callback<sovchilar.uz.domain.models.remote.auth.AuthResponseModel> {
//                override fun onResponse(
//                    call: Call<sovchilar.uz.domain.models.remote.auth.AuthResponseModel>, response: Response<sovchilar.uz.domain.models.remote.auth.AuthResponseModel>
//                ) {
//
//                    if (response.isSuccessful) {
//                        response.body()?.let {
//                            loginLiveData.postValue(
//                                sovchilar.uz.domain.models.remote.auth.AuthStateModel(
//                                    sovchilar.uz.domain.models.remote.auth.AuthState.AUTHENTICATED,
//                                    it.token
//                                )
//                            )
//                        } ?: loginLiveData.postValue(
//                            sovchilar.uz.domain.models.remote.auth.AuthStateModel(
//                                sovchilar.uz.domain.models.remote.auth.AuthState.INVALID_AUTHENTICATION,
//                                null
//                            )
//                        )
//                    } else {
//                        if (response.code() == 400) {
//                            val gson = Gson()
//                            val errorResponse = gson.fromJson(
//                                response.errorBody()?.charStream(),
//                                sovchilar.uz.domain.models.remote.auth.AuthErrorModel::class.java
//                            )
//                            loginLiveData.postValue(
//                                sovchilar.uz.domain.models.remote.auth.AuthStateModel(
//                                    sovchilar.uz.domain.models.remote.auth.AuthState.INVALID_AUTHENTICATION,
//                                    null,
//                                    errorResponse.message
//                                )
//                            )
//                        } else {
//                            loginLiveData.postValue(
//                                sovchilar.uz.domain.models.remote.auth.AuthStateModel(
//                                    sovchilar.uz.domain.models.remote.auth.AuthState.UNAUTHENTICATED,
//                                    null,
//                                    "Error code: ${response.code()}"
//                                )
//                            )
//                        }
//                    }
//                }
//
//                override fun onFailure(call: Call<sovchilar.uz.domain.models.remote.auth.AuthResponseModel>, t: Throwable) {
//                    loginLiveData.postValue(
//                        sovchilar.uz.domain.models.remote.auth.AuthStateModel(
//                            sovchilar.uz.domain.models.remote.auth.AuthState.CONNECTION_ERROR,
//                            null,
//                            t.message
//                        )
//                    )
//                }
//            })
    }

    suspend fun saveCredentials(
        encryptedSharedPrefsUseCase: EncryptedSharedPrefsUseCase,
        loginText: String,
        passwordText: String,
        tokenText: String
    ) {
        encryptedSharedPrefsUseCase.writeIntoFile(
            sovchilar.uz.comm.login, loginText
        )
        encryptedSharedPrefsUseCase.writeIntoFile(
            sovchilar.uz.comm.password, passwordText
        )
        encryptedSharedPrefsUseCase.writeIntoFile(sovchilar.uz.comm.token, tokenText)
        encryptedSharedPrefsUseCase.saveAuthState(sovchilar.uz.comm.authenticated)
    }

}
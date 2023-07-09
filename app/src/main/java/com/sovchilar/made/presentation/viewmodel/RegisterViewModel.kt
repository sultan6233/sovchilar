package com.sovchilar.made.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.sovchilar.made.data.local.usecases.EncryptedSharedPrefsUseCase
import com.sovchilar.made.data.remote.ApiService
import com.sovchilar.made.domain.models.remote.auth.AuthErrorModel
import com.sovchilar.made.domain.models.remote.auth.AuthModel
import com.sovchilar.made.domain.models.remote.auth.AuthResponseModel
import com.sovchilar.made.domain.models.remote.auth.AuthState
import com.sovchilar.made.domain.models.remote.auth.AuthStateModel
import com.sovchilar.made.uitls.login
import com.sovchilar.made.uitls.password
import com.sovchilar.made.uitls.token
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterViewModel : ViewModel() {
    val loginLiveData = MutableLiveData<AuthStateModel>()
    val loginErrorLiveData = MutableLiveData<String>()
    init {

    }
    private suspend fun loginOrRegisterRequest(login: String, password: String) = channelFlow {
        ApiService.create().loginOrRegister(AuthModel(login, password))
            .enqueue(object : Callback<AuthResponseModel> {
                override fun onResponse(
                    call: Call<AuthResponseModel>, response: Response<AuthResponseModel>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.let {
                            viewModelScope.launch {
                                send(it)
                                close()
                            }
                        }
                    } else {
                        Log.d("Login", "onResponse: ${response.errorBody()}")
                        val gson = Gson()
                        val errorResponse = gson.fromJson(
                            response.errorBody()?.charStream(),
                            AuthErrorModel::class.java
                        )
                        viewModelScope.launch {
                            loginErrorLiveData.postValue(errorResponse.message)
                            send(null)
                            close()
                        }
                    }
                }

                override fun onFailure(call: Call<AuthResponseModel>, t: Throwable) {
                    viewModelScope.launch {
                        send(null)
                        close()
                    }
                }
            })
        awaitClose()
    }.flowOn(Dispatchers.IO)

    fun loginOrRegister(login: String, password: String) {
        viewModelScope.launch {
            loginOrRegisterRequest(login, password).collect {
                withContext(Dispatchers.IO) {
                    it?.let {
                        loginLiveData.postValue(AuthStateModel(AuthState.AUTHENTICATED, it.token))
                    } ?: loginLiveData.postValue(AuthStateModel(AuthState.INVALID_AUTHENTICATION,null))
                }
            }
        }
    }

    fun saveCredentials(
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
    }

    override fun onCleared() {
        super.onCleared()

    }
}
package com.sovchilar.made.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sovchilar.made.data.remote.ApiService
import com.sovchilar.made.domain.models.remote.auth.AuthModel
import com.sovchilar.made.domain.models.remote.auth.AuthResponseModel
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
    val loginLiveData = MutableLiveData<String?>()
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
                        viewModelScope.launch {
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
                        loginLiveData.postValue(it.token)
                    }?:loginLiveData.postValue(null)
                }
            }
        }

    }
}
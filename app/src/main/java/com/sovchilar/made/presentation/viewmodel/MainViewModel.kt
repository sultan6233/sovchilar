package com.sovchilar.made.presentation.viewmodel

import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sovchilar.made.EncryptedSharedPrefsUseCase
import com.sovchilar.made.presentation.fragments.view.AdvertisementFragment
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import sovchilar.uz.domain.models.remote.auth.AuthModel
import sovchilar.uz.domain.models.remote.auth.AuthState
import sovchilar.uz.domain.usecases.RegisterUserUsecase
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val registerUserUsecase: RegisterUserUsecase) :
    ViewModel() {
    val dataReady = MutableLiveData<Boolean>()
    private val _loginLiveData = MutableLiveData<AuthState>()
    val loginLiveData: LiveData<AuthState> get() = _loginLiveData

    var loginFromStart = true

    fun loginOrRegister(authModel: AuthModel) = runBlocking(Dispatchers.IO) {
        registerUserUsecase.invoke(authModel).collectLatest {
            _loginLiveData.postValue(it)
        }
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
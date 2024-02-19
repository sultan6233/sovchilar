package com.sovchilar.made.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sovchilar.made.EncryptedSharedPrefsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import sovchilar.uz.domain.models.remote.auth.AuthModel
import sovchilar.uz.domain.models.remote.auth.AuthState
import sovchilar.uz.domain.usecases.RegisterUserUsecase
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(private val registerUserUsecase: RegisterUserUsecase) :
    ViewModel() {
    private val _loginLiveData = MutableLiveData<AuthState>()
    val loginLiveData:LiveData<AuthState> get() = _loginLiveData

    fun loginOrRegister(authModel: AuthModel) = viewModelScope.launch(Dispatchers.IO) {
        registerUserUsecase.invoke(authModel).collectLatest {
            _loginLiveData.postValue(it)
        }
    }

    fun saveCredentials(
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
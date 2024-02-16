package com.sovchilar.made.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import sovchilar.uz.domain.models.UserModel
import sovchilar.uz.domain.models.remote.AdvertisementsModel
import sovchilar.uz.domain.usecases.GetAdvertisementsUseCase
import sovchilar.uz.domain.utils.DataState
import javax.inject.Inject

@HiltViewModel
class AdvertisementViewModel @Inject constructor(private val getAdvertisementsUseCase: GetAdvertisementsUseCase) :
    ViewModel() {
    val gender = MutableLiveData<String>()


    private val _advertisements = MutableLiveData<DataState<UserModel>>()
    val advertisements: LiveData<DataState<UserModel>> get() = _advertisements

      var advertisementsList: ArrayList<AdvertisementsModel>? = ArrayList()

    init {
        getAdvertisements()
    }

    fun getAdvertisements() = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            getAdvertisementsUseCase.invoke().collect { dataState ->
                _advertisements.postValue(dataState)
            }
        }
    }

}
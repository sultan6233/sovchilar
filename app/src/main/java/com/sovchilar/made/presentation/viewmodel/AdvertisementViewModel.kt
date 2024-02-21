package com.sovchilar.made.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import sovchilar.uz.comm.femaleGender
import sovchilar.uz.domain.models.UserModel
import sovchilar.uz.domain.models.remote.AdvertisementsModel
import sovchilar.uz.domain.usecases.GetAdvertisementsUseCase
import sovchilar.uz.domain.utils.DataState
import javax.inject.Inject

@HiltViewModel
class AdvertisementViewModel @Inject constructor(val getAdvertisementsUseCase: GetAdvertisementsUseCase) :
    ViewModel() {
    val gender = MutableLiveData<String>()


    private val _advertisements = MutableLiveData<PagingData<AdvertisementsModel>>()
    val advertisements: LiveData<PagingData<AdvertisementsModel>> get() = _advertisements

    init {
        getAdvertisements(femaleGender)
    }

    fun getAdvertisements(gender: String) = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            getAdvertisementsUseCase.invoke(gender).cachedIn(viewModelScope).collectLatest { dataState ->
                _advertisements.postValue(dataState)
            }
        }
    }

}
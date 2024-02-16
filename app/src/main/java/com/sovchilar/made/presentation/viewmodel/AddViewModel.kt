package com.sovchilar.made.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import sovchilar.uz.domain.models.PostResponse
import sovchilar.uz.domain.models.remote.AdvertisementsModel
import sovchilar.uz.domain.usecases.PostAdvertisementUseCase
import sovchilar.uz.domain.utils.DataState

class AddViewModel(private val postAdvertisementUseCase: PostAdvertisementUseCase) : ViewModel() {
    private val _postResponse = MutableStateFlow<DataState<PostResponse>>(DataState.Loading)
    val postResponse: StateFlow<DataState<PostResponse>> = _postResponse.asStateFlow()

    fun postAdvertisement(authToken: String, advertisement: AdvertisementsModel) {
        viewModelScope.launch {
//            advertisementRepository.postAdvertisement(authToken, advertisement)
//                .collect { dataState ->
//                    _postResponse.value = dataState
//                }
        }
    }
}
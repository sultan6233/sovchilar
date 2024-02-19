package com.sovchilar.made.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import sovchilar.uz.domain.models.PostResponse
import sovchilar.uz.domain.models.remote.AdvertisementsModel
import sovchilar.uz.domain.usecases.PostAdvertisementUseCase
import sovchilar.uz.domain.utils.DataState
import javax.inject.Inject

@HiltViewModel
class AddViewModel @Inject constructor(private val postAdvertisementUseCase: PostAdvertisementUseCase) :
    ViewModel() {
    private val _postResponse = MutableLiveData<DataState<PostResponse>>(DataState.Loading)
    val postResponse: LiveData<DataState<PostResponse>> get() = _postResponse

    fun postAdvertisement(authToken: String, advertisement: AdvertisementsModel) {
        viewModelScope.launch(Dispatchers.IO) {
            postAdvertisementUseCase.invoke(authToken, advertisement).collectLatest {
                _postResponse.postValue(it)
            }
        }
    }
}
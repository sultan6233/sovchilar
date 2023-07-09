package com.sovchilar.made.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sovchilar.made.data.remote.ApiService
import com.sovchilar.made.domain.models.AdvertisementsModel
import com.sovchilar.made.domain.models.UserModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AdvertisementViewModel : ViewModel() {
    val gender = MutableLiveData<String>()

    val advertisements = MutableLiveData<List<AdvertisementsModel>>()

    var advertisementsList:ArrayList<AdvertisementsModel>? = ArrayList()

    init {
        getAdvertisements()
    }

    private fun getAdvertisementsRequest() = channelFlow {
        ApiService.create().getAdvertisements().enqueue(object : Callback<UserModel> {
            override fun onResponse(call: Call<UserModel>, response: Response<UserModel>) {
                response.body()?.let {
                    viewModelScope.launch {
                        send(it.personals)
                        close()
                    }
                } ?: viewModelScope.launch { send(null) }
            }

            override fun onFailure(call: Call<UserModel>, t: Throwable) {
                viewModelScope.launch {
                    Log.d("suka",t.localizedMessage.toString())
                    send(null)
                    close()
                }
            }
        })
        awaitClose()
    }.flowOn(Dispatchers.IO)

    private fun getAdvertisements() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                getAdvertisementsRequest().collect {
                    advertisements.postValue(it)
                }
            }
        }
    }

}
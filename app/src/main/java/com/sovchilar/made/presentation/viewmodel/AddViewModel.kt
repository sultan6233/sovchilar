package com.sovchilar.made.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sovchilar.made.data.remote.ApiService
import com.sovchilar.made.domain.models.remote.payment.PaymentPriceResponseModel
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class AddViewModel @Inject constructor() : ViewModel() {
    val priceLiveData = MutableLiveData<String>()
    suspend fun postAdvertisement() {

    }

    fun getPriceRequest(authToken: String) {
        ApiService.create().getPrice("Bearer $authToken").enqueue(object : Callback<PaymentPriceResponseModel> {

            override fun onResponse(
                call: Call<PaymentPriceResponseModel>,
                response: Response<PaymentPriceResponseModel>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        priceLiveData.postValue(it.price.toInt()    .toString())
                    }
                } else {

                }
            }

            override fun onFailure(call: Call<PaymentPriceResponseModel>, t: Throwable) {

            }
        })
    }

}
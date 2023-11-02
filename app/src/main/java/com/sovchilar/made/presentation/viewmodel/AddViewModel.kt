package com.sovchilar.made.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sovchilar.made.data.remote.ApiService
import com.sovchilar.made.domain.models.AdvertisementsModel
import com.sovchilar.made.domain.models.PostResponse
import com.sovchilar.made.domain.models.remote.payment.PaymentPriceResponseModel
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class AddViewModel @Inject constructor() : ViewModel() {
    val priceLiveData = MutableLiveData<String>()
    val advertisementAddedLiveData = MutableLiveData<PostResponse?>()
    suspend fun postAdvertisement(authToken: String, advertisementModel: AdvertisementsModel) {
        ApiService.create().postAdvertisement("Bearer $authToken", advertisementModel)
            .enqueue(object : Callback<PostResponse> {
                override fun onResponse(
                    call: Call<PostResponse>,
                    response: Response<PostResponse>
                ) {
                    if (response.isSuccessful){
                        response.body()?.let {
                            advertisementAddedLiveData.postValue(it)
                        }?:advertisementAddedLiveData.postValue(null)
                    }
                    else{
                        advertisementAddedLiveData.postValue(null)
                    }
                }

                override fun onFailure(call: Call<PostResponse>, t: Throwable) {
                    advertisementAddedLiveData.postValue(null)
                }

            })
    }

    fun getPriceRequest(authToken: String) {
        ApiService.create().getPrice("Bearer $authToken")
            .enqueue(object : Callback<PaymentPriceResponseModel> {

                override fun onResponse(
                    call: Call<PaymentPriceResponseModel>,
                    response: Response<PaymentPriceResponseModel>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.let {
                            priceLiveData.postValue(it.price.toInt().toString())
                        }
                    } else {

                    }
                }

                override fun onFailure(call: Call<PaymentPriceResponseModel>, t: Throwable) {

                }
            })
    }

}
package com.sovchilar.made.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sovchilar.made.data.remote.ApiService
import com.sovchilar.made.domain.models.CardModel
import com.sovchilar.made.domain.models.remote.payment.PaymentConfirmModel
import com.sovchilar.made.domain.models.remote.payment.PaymentConfirmResponseModel
import com.sovchilar.made.domain.models.remote.payment.PaymentModel
import com.sovchilar.made.domain.models.remote.payment.PaymentResponseModel
import com.sovchilar.made.domain.models.remote.payment.PaymentResultModel
import com.sovchilar.made.domain.usecases.DateUseCase
import com.sovchilar.made.presentation.usecases.Card
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class PayViewModel @Inject constructor(private val card: Card, val dateUseCase: DateUseCase) :
    ViewModel() {
    val paymentLiveData = MutableLiveData<PaymentResultModel?>()
    var paymentResult: PaymentResultModel? = null
    var paymentConfirmResult = MutableLiveData<PaymentConfirmResponseModel?>()
    fun provideCard(input: String, size: Int): CardModel {
        return card.validateCard(input, size)
    }

    fun payRequest(cardNumber: String, expireDate: String, authToken: String) {
        ApiService.create().pay("Bearer $authToken", PaymentModel(cardNumber, expireDate))
            .enqueue(object : Callback<PaymentResultModel> {

                override fun onResponse(
                    call: Call<PaymentResultModel>, response: Response<PaymentResultModel>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.let { paymentResultModel ->
                            paymentLiveData.value = paymentResultModel
                        } ?: paymentLiveData.postValue(null)
                    } else {
                        paymentLiveData.postValue(null)
                    }
                }

                override fun onFailure(call: Call<PaymentResultModel>, t: Throwable) {
                    paymentLiveData.postValue(null)
                }
            })
    }

    fun confirmPaymentRequest(code: String, session: Int, authToken: String) {
        ApiService.create().confirmPayment(authToken, PaymentConfirmModel(session, code))
            .enqueue(object : Callback<PaymentConfirmResponseModel> {
                override fun onResponse(
                    call: Call<PaymentConfirmResponseModel>,
                    response: Response<PaymentConfirmResponseModel>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.let { paymentConfirmResponseModel ->
                            paymentConfirmResult.postValue(paymentConfirmResponseModel)
                        } ?: paymentConfirmResult.postValue(null)
                    } else {
                        paymentConfirmResult.postValue(null)
                    }
                }

                override fun onFailure(call: Call<PaymentConfirmResponseModel>, t: Throwable) {
                    paymentConfirmResult.postValue(null)
                }

            })
    }
}
package com.sovchilar.made.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sovchilar.made.presentation.usecases.Card
import sovchilar.uz.domain.models.CardModel
import sovchilar.uz.domain.models.remote.payment.PaymentConfirmResponseModel
import sovchilar.uz.domain.models.remote.payment.PaymentResultModel
import sovchilar.uz.domain.usecases.DateUseCase

class PayViewModel :
    ViewModel() {
    private val card by lazy { Card() }
    val dateUseCase by lazy { DateUseCase() }
    val paymentLiveData = MutableLiveData<PaymentResultModel?>()
    var paymentResult: PaymentResultModel? = null
    var paymentConfirmResult = MutableLiveData<PaymentConfirmResponseModel?>()
    fun provideCard(input: String, size: Int): CardModel {
        return card.validateCard(input, size)
    }

    fun payRequest(cardNumber: String, expireDate: String, authToken: String) {
//        sovchilar.arch.featureremoteapi.ApiService.create().pay("Bearer $authToken",
//            PaymentModel(cardNumber, expireDate)
//        )
//            .enqueue(object : Callback<PaymentResultModel> {
//
//                override fun onResponse(
//                    call: Call<PaymentResultModel>, response: Response<PaymentResultModel>
//                ) {
//                    if (response.isSuccessful) {
//                        response.body()?.let { paymentResultModel ->
//                            paymentLiveData.value = paymentResultModel
//                        } ?: paymentLiveData.postValue(null)
//                    } else {
//                        paymentLiveData.postValue(null)
//                    }
//                }
//
//                override fun onFailure(call: Call<PaymentResultModel>, t: Throwable) {
//                    paymentLiveData.postValue(null)
//                }
//            })
    }

    fun confirmPaymentRequest(code: String, session: Int, authToken: String) {
//        sovchilar.arch.featureremoteapi.ApiService.create().confirmPayment("Bearer $authToken",
//            PaymentConfirmModel(session, code)
//        )
//            .enqueue(object : Callback<PaymentConfirmResponseModel> {
//                override fun onResponse(
//                    call: Call<PaymentConfirmResponseModel>,
//                    response: Response<PaymentConfirmResponseModel>
//                ) {
//                    if (response.isSuccessful) {
//                        response.body()?.let { paymentConfirmResponseModel ->
//                            paymentConfirmResult.postValue(paymentConfirmResponseModel)
//                        } ?: paymentConfirmResult.postValue(null)
//                    } else {
//                        paymentConfirmResult.postValue(null)
//                    }
//                }
//
//                override fun onFailure(call: Call<PaymentConfirmResponseModel>, t: Throwable) {
//                    paymentConfirmResult.postValue(null)
//                }
//
//            })
    }
}
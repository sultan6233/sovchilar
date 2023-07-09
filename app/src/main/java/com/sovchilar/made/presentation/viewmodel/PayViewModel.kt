package com.sovchilar.made.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.sovchilar.made.data.remote.PaymentApiService
import com.sovchilar.made.domain.models.CardModel
import com.sovchilar.made.domain.models.remote.payment.PaymentWithoutRegistrationModel
import com.sovchilar.made.domain.models.remote.payment.PaymentWithoutRegistrationResponseModel
import com.sovchilar.made.presentation.usecases.Card
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class PayViewModel @Inject constructor(private val card: Card) : ViewModel() {
    fun provideCard(input: String, size: Int): CardModel {
        return card.validateCard(input, size)
    }

    private val headers = HashMap<String, String>()

    init {
        initPaymentHeaders()
    }

    fun testRequest() {
        PaymentApiService.create().getInfo().enqueue(object : Callback<Any> {
            override fun onResponse(call: Call<Any>, response: Response<Any>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        Log.d("suka", it.toString())
                    }
                }
            }

            override fun onFailure(call: Call<Any>, t: Throwable) {
                Log.d("suka", t.localizedMessage)
            }

        })
    }

    suspend fun payRequest(amount: Double, cardNumber: String, expireDate: String) {
        PaymentApiService.create().paymentWithoutRegistration(
            PaymentWithoutRegistrationModel(
                amount,
                cardNumber,
                expireDate,
                System.currentTimeMillis().toString()
            )
        ).enqueue(object : Callback<PaymentWithoutRegistrationResponseModel> {
            override fun onResponse(
                call: Call<PaymentWithoutRegistrationResponseModel>,
                response: Response<PaymentWithoutRegistrationResponseModel>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        Log.d("suka", it.toString())
                    }
                }
            }

            override fun onFailure(
                call: Call<PaymentWithoutRegistrationResponseModel>,
                t: Throwable
            ) {
                Log.d("suka", t.localizedMessage)
            }

        })
    }

    fun initPaymentHeaders() {
        headers["Content-Type"] = "application/json; charset=utf-8"
        headers["Accept"] = "application/json"
        headers["Authorization"] = "Basic Y3VwZXJ0aW5vc2hhaGdyb3VwOmVZeEFAdjE1OCYwQDZiMWU="
        headers["Language"] = "ru"
    }
}
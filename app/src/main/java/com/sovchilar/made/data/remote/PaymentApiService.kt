package com.sovchilar.made.data.remote

import android.util.Base64
import com.sovchilar.made.domain.models.remote.payment.PaymentWithoutRegistrationModel
import com.sovchilar.made.domain.models.remote.payment.PaymentWithoutRegistrationResponseModel
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.Headers
import retrofit2.http.POST
import java.io.IOException

interface PaymentApiService {


    @POST("api/Payment/paymentWithoutRegistration")
    fun paymentWithoutRegistration(
        @Body paymentWithoutRegistrationModel: PaymentWithoutRegistrationModel
    ): Call<PaymentWithoutRegistrationResponseModel>

    @GET("api/Payment/paymentWithoutRegistration")
    fun getInfo(
    ): Call<Any>

    companion object {
        var BASE_URL = "https://pay.myuzcard.uz/"

        fun create(): PaymentApiService {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            val client =
                OkHttpClient.Builder().addInterceptor(interceptor)
                    .build()
            val retrofit =
                Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).client(client)
                    .baseUrl(BASE_URL).build()
            return retrofit.create(PaymentApiService::class.java)
        }
    }

}
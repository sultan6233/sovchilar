package com.sovchilar.made.data.remote

import com.sovchilar.made.domain.models.AdvertisementsModel
import com.sovchilar.made.domain.models.PostResponse
import com.sovchilar.made.domain.models.UserModel
import com.sovchilar.made.domain.models.remote.auth.AuthModel
import com.sovchilar.made.domain.models.remote.auth.AuthResponseModel
import com.sovchilar.made.domain.models.remote.payment.PaymentConfirmModel
import com.sovchilar.made.domain.models.remote.payment.PaymentConfirmResponseModel
import com.sovchilar.made.domain.models.remote.payment.PaymentModel
import com.sovchilar.made.domain.models.remote.payment.PaymentPriceResponseModel
import com.sovchilar.made.domain.models.remote.payment.PaymentResponseModel
import com.sovchilar.made.domain.models.remote.payment.PaymentResultModel
import okhttp3.JavaNetCookieJar
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import java.net.CookieManager
import java.net.CookiePolicy
import java.util.Arrays


interface ApiService {

    @POST("api/personals")
    fun postAdvertisement(
        @Header("Authorization") authToken: String,
        @Body postAdvertisement: AdvertisementsModel
    ): Call<PostResponse>

    @GET("api/personals/all")
    fun getAdvertisements(): Call<UserModel>

    @POST("api/auth")
    fun loginOrRegister(@Body authModel: AuthModel): Call<AuthResponseModel>

    @POST("api/payment/make")
    fun pay(
        @Header("Authorization") authToken: String, @Body paymentModel: PaymentModel
    ): Call<PaymentResultModel>

    @POST("api/payment/confirm")
    fun confirmPayment(
        @Header("Authorization") authToken: String, @Body
        paymentConfirmModel: PaymentConfirmModel
    ): Call<PaymentConfirmResponseModel>

    @GET("api/payment/price")
    fun getPrice(@Header("Authorization") authToken: String): Call<PaymentPriceResponseModel>

    companion object {
        var BASE_URL = "http://176.96.241.238:3333/"

        fun create(): ApiService {


            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            val client = OkHttpClient.Builder().addInterceptor(interceptor)
                .protocols(listOf(Protocol.HTTP_1_1)).build()
            val retrofit =
                Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).client(client)
                    .baseUrl(BASE_URL).build()
            return retrofit.create(ApiService::class.java)
        }
    }

}

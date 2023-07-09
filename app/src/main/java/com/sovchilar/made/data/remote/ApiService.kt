package com.sovchilar.made.data.remote

import com.sovchilar.made.domain.models.AdvertisementsModel
import com.sovchilar.made.domain.models.PostResponse
import com.sovchilar.made.domain.models.UserModel
import com.sovchilar.made.domain.models.remote.auth.AuthModel
import com.sovchilar.made.domain.models.remote.auth.AuthResponseModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST


interface ApiService {

    @POST("api/user")
    fun postAdvertisement(@Body postAdvertisement: AdvertisementsModel): Call<PostResponse>

    @GET("api/personals/all")
    fun getAdvertisements(): Call<UserModel>

    @POST("api/auth")
    fun loginOrRegister(@Body authModel: AuthModel): Call<AuthResponseModel>

    companion object {
        var BASE_URL = "http://176.96.241.238:3333/"

        fun create(): ApiService {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
            val retrofit =
                Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).client(client)
                    .baseUrl(BASE_URL).build()
            return retrofit.create(ApiService::class.java)
        }
    }

}
